package com.d204.rumeet.game.model.service;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.exception.DuplicateFriendRequestException;
import com.d204.rumeet.exception.InvalidRunningException;
import com.d204.rumeet.exception.NoUserDataException;
import com.d204.rumeet.exception.TerminatedRunningException;
import com.d204.rumeet.fcm.model.service.FcmMessageService;
import com.d204.rumeet.friend.model.dao.FriendRequestDao;
import com.d204.rumeet.game.model.dto.*;
import com.d204.rumeet.game.model.mapper.GameMapper;
import com.d204.rumeet.tools.FriendMatchingTool;
import com.d204.rumeet.tools.OSUpload;
import com.d204.rumeet.user.model.dto.SimpleUserDto;
import com.d204.rumeet.user.model.dto.SimpleUserFcmDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameMapper gameMapper;
    private final KafkaService kafkaService;

    @Autowired
    private final FcmMessageService fcmMessageService;

    private final UserService userService;

    private final MongoTemplate mongoTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final FriendMatchingTool friendMatchingTool;

    private final OSUpload osUpload;

    public void makeRace(RaceDto raceDto) {
        gameMapper.makeRace(raceDto);
    }

    @Override
    public int inviteRace(RaceDto raceDto) {
        // DB에 state 0으로 추가
        raceDto.setState(0);
        gameMapper.makeRace(raceDto);

        int userId = raceDto.getUserId();
        int partnerId = raceDto.getPartnerId();

        SimpleUserDto user = userService.getSimpleUserById(userId);
        SimpleUserFcmDto target = userService.getSimpleUserFcmInfoById(partnerId);

        // mongoDB에 초대 저장하기
        FriendRaceDto matchRequest = FriendRaceDto.builder()
                .raceId(raceDto.getId()) // Mysql에 저장된 id
                .userId(userId)
                .partnerId(partnerId)
                .mode(raceDto.getMode())
                .date(raceDto.getDate())
                .state(0) // default state : 0
                .build();
        mongoTemplate.insert(matchRequest);

        if (target.getMatchingAlarm() == 1) { // 알람 수신 허용일 경우에만 FCM 전송
            try {
                // 생성된 race ID로 fcm 보내기
                fcmMessageService.sendMessageTo(
                        target.getFcmToken(),
                        "매칭 초대",
                        user.getNickname() + "님이 함께 달리자고 합니다!",
                        0
                );
            } catch (Exception e) {
            }
        }
        return raceDto.getId();
    }

    @Override
    public void acceptRace(int raceId) {
        // mongoDB에서 roomId로 매칭 정보 가져오기
        FriendRaceDto request = mongoTemplate.findOne(
                Query.query(Criteria.where("raceId").is(raceId)),
                FriendRaceDto.class
        );
        if (request == null) {
            throw new InvalidRunningException();
        } else if (request.getState() == -1) {
            throw new TerminatedRunningException();
        } else { // friend.queue에 raceId 넣어주기
            Gson gson = new Gson();
            rabbitTemplate.convertAndSend("game.exchange", "friend", gson.toJson(raceId));
            // mongoDB state = 1로 바꿔주기
            mongoTemplate.updateMulti(
                    Query.query(Criteria.where("raceId").is(raceId)),
                    Update.update("state", 1),
                    FriendRaceDto.class
            );
        }
    }

    @Override
    public void denyRace(int raceId) {
        // 러닝 초대 거부 (state -1로 변경)
        int result = gameMapper.denyRace(raceId);
        if (result != 1) {
            throw new TerminatedRunningException();
        } else { // 성공 : mongoDB state = -1로 변경
            mongoTemplate.updateMulti(
                    Query.query(Criteria.where("raceId").is(raceId)),
                    Update.update("state", -1),
                    FriendRaceDto.class
            );
        }
    }


    @Override
    public int getRaceState(int raceId) {
        RaceStateDto result = gameMapper.getRaceState(raceId);
        System.out.println("################get Race State result: " + result);
        if (result == null) {
            throw new InvalidRunningException();
        }
        return result.getState();
    }

    @Override
    public List<FriendRaceInfoDto> getInvitationList(int userId) {
        List<FriendRaceDto> requests = mongoTemplate.find(
                Query.query(Criteria.where("partnerId").is(userId).and("state").is(0)),
                FriendRaceDto.class
        );
        List<FriendRaceInfoDto> list = new ArrayList<>();
        for (FriendRaceDto dto : requests) {
            UserDto user = userService.getUserById(dto.getUserId());
            FriendRaceInfoDto tmp = FriendRaceInfoDto.builder()
                    .raceId(dto.getRaceId())
                    .state(dto.getState())
                    .partnerId(dto.getPartnerId())
                    .nickname(user.getNickname())
                    .profile(user.getProfile())
                    .mode(dto.getMode())
                    .date(dto.getDate())
                    .userId(dto.getUserId()
                    ).build();
            list.add(tmp);
        }
        //

        return list;
    }

    @Override
    public void endGameToKafka(Message message) throws Exception {
        String msg = new String(message.getBody());
        Type type = new TypeToken<Map<String, Integer>>() {
        }.getType();
        Map<String, Integer> map = new Gson().fromJson(msg, type);
        int mode = gameMapper.getMode(map.get("race_id"));
        int[] km = new int[]{1, 2, 3, 5, 1, 2, 3, 5, 1, 2, 3, 5};
        kafkaService.sendMessage("rumeet.endgame." + km[mode], msg);
    }


    @Override
    public SoloPlayDto doSoloPlay(int userId, int mode, int ghost) {
        int[] km = new int[]{1, 2, 3, 5};
        SoloPlayDto soloPlayDto = new SoloPlayDto();

        RaceDto raceDto = new RaceDto();
        raceDto.setMode(mode);
        soloPlayDto.setMode(mode);
        raceDto.setUserId(userId);
        soloPlayDto.setUserId(userId);
        raceDto.setPartnerId(-1);
        soloPlayDto.setPartnerId(-1);
        raceDto.setDate(System.currentTimeMillis());
        soloPlayDto.setDate(raceDto.getDate());
        int[] pace = new int[km[mode]];
        Arrays.fill(pace,-1);
        if (ghost == 1) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://119.202.203.157:8001/recommend/" + km[mode] + "/" + userId + "/1").get().build();
            Call call = client.newCall(request);
            String responseBody = "";
            RespData<String> data = new RespData<>();
            try {
                Response response = call.execute();
                responseBody = response.body().string();
                System.out.println("responseBody = " + responseBody);
                data.setData(responseBody);
            } catch (IOException e) {
                System.out.println("e = " + e);
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<List<GamePaceDto>>() {
            }.getType();
            List<GamePaceDto> gamePaceDtos = gson.fromJson(responseBody, listType);
            GamePaceDto user = gamePaceDtos.get(0);
            raceDto.setPartnerId(user.getId());
            soloPlayDto.setPartnerId(user.getId());
            pace = user.getPace();
            if(pace == null || pace.length == 0) {
                pace = new int[km[mode]];
                for (int i = 0; i < km[mode]; i++) {
                    pace[i] = 300;
                }
            }
            soloPlayDto.setPace(pace);
        } else if (ghost == 2) {
            GamePaceDto gamePaceDto = kafkaService.messageBYFastApi(km[mode], userId);
            raceDto.setPartnerId(userId);
            soloPlayDto.setPartnerId(userId);
            pace = gamePaceDto.getPace();
            if(pace == null) {
                pace = new int[km[mode]];
                for (int i = 0; i < km[mode]; i++) {
                    pace[i] = 300;
                }
            }
        }
        for (int i = 0; i < km[mode]; i++) {
            if(pace[i]==-1) {
                pace[i] = 300;
            }
        }
        soloPlayDto.setPace(pace);
        gameMapper.makeRace(raceDto);
        soloPlayDto.setId(raceDto.getId());
        return soloPlayDto;
    }

    @Override
    public List<RecommendDto> recommendMainPage(int userId) {
        List<RecommendDto> list = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        int[] km = new int[]{1, 2, 3, 5};
        Random r = new Random();
        int seed = r.nextInt(4);
        Request request = new Request.Builder().url("http://119.202.203.157:8001/recommend/" + km[seed] + "/" + userId + "/3").get().build();
        Call call = client.newCall(request);
        String responseBody = "";
        RespData<String> data = new RespData<>();
        try {
            Response response = call.execute();
            responseBody = response.body().string();
            System.out.println("responseBody = " + responseBody);
            data.setData(responseBody);
        } catch (IOException e) {
            System.out.println("e = " + e);
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GamePaceDto>>() {
        }.getType();
        List<GamePaceDto> gamePaceDtos = gson.fromJson(responseBody, listType);

        for(GamePaceDto gamePaceDto : gamePaceDtos) {
            UserDto user = userService.getUserById(gamePaceDto.getId());
            RecommendDto recommendDto = new RecommendDto(user.getId(), user.getNickname(), user.getProfile());
            list.add(recommendDto);
        }

        return list;
    }

    @Override
    public String savePoly(MultipartFile file) {
        String url = "https://kr.object.ncloudstorage.com/rumeet/polyline/1309605582565100.png";
        if(file != null && !file.isEmpty()) {
            String [] formats = {".jpeg", ".png", ".bmp", ".jpg", ".PNG", ".JPEG"};
            // 원래 파일 이름 추출
            String origName = file.getOriginalFilename();
            System.out.println(origName);
            // 확장자 추출(ex : .png)
            String extension = ".png";

            String folderName = "polyline";
            for(int i = 0; i < formats.length; i++) {
                if (extension.equals(formats[i])){
                    File uploadFile = null;
                    try {
                        uploadFile = osUpload.convert(file)        // 파일 생성
                                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String fileName = folderName + "/" + System.nanoTime() + ".png";
                    osUpload.put("rumeet", fileName, uploadFile);

                    url = "https://kr.object.ncloudstorage.com/rumeet/"+fileName;
                    break;
                }
            }
        }
        return url;
    }
}
