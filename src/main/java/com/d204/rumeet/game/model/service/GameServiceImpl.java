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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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


    public void makeRace(RaceDto raceDto) {
        gameMapper.makeRace(raceDto);
    }

    @Override
    public void inviteRace(RaceDto raceDto) {
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
        }
    }

    @Override
    public void denyRace(int raceId) {
        // 러닝 초대 거부 (state -1로 변경)
        int result = gameMapper.denyRace(raceId);
        if (result != 1) {
            throw new TerminatedRunningException();
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
                Query.query(Criteria.where("partnerId").is(userId)),
                FriendRaceDto.class
        );
        List<FriendRaceInfoDto> list = new ArrayList<>();
        for (FriendRaceDto dto : requests) {
            UserDto user = userService.getUserById(dto.getUserId());
            FriendRaceInfoDto tmp = FriendRaceInfoDto.builder()
                    .raceId(dto.getRaceId())
                    .state(dto.getState())
                    .partnerId(userId)
                    .nickname(user.getNickname())
                    .profile(user.getProfile())
                    .mode(dto.getMode())
                    .date(dto.getDate())
                    .userId(dto.getPartnerId()).build();
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
        soloPlayDto.setPartnerId(userId);
        raceDto.setDate(System.currentTimeMillis());
        soloPlayDto.setDate(raceDto.getDate());
        int[] pace = new int[km[mode]];
        if (ghost == 1) {
            OkHttpClient client = new OkHttpClient();
            System.out.println("http://119.202.203.157:8001/recommend/" + km[mode] + "/" + userId + "/1");
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
            soloPlayDto.setPace(pace);
        }
        gameMapper.makeRace(raceDto);
        soloPlayDto.setId(raceDto.getId());
        return soloPlayDto;
    }
}
