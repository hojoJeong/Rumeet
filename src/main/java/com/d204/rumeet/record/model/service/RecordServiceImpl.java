package com.d204.rumeet.record.model.service;

import com.d204.rumeet.badge.model.service.BadgeService;
import com.d204.rumeet.record.model.dto.*;
import com.d204.rumeet.record.model.mapper.RecordMapper;
import com.d204.rumeet.tools.OSUpload;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordMapper recordMapper;
    private final UserService userService;
    final String bucketName = "rumeet";
    private final OSUpload osUpload;
    @Override
    public RecordDto getRecord(int userId) {
        RecordDto record = recordMapper.getRecord(userId);
        return record;
    }

    @Override
    public void updateRecord(RaceInfoReqDto raceInfoReqDto)  {

        //{"user_id":1, "mode":2, "success":1, "elapsed_time":1234}
        int mode = raceInfoReqDto.getMode();

        int userId = raceInfoReqDto.getUserId();
        int success = raceInfoReqDto.getSuccess();
        int elapsedTime = raceInfoReqDto.getTime();
        // 기존 정보
        RecordDto record = recordMapper.getRecord(userId);
        double originPace = record.getAveragePace();
        int originCount = record.getTotalCount();
        int completeSuccess = record.getCompetitionSuccessCount();
        int teamSuccess = record.getTeamSuccessCount();
        double averagePace = 0.0;

        if (mode >= 4 && mode <= 7 && success == 1) { //경쟁모드 승리
            completeSuccess++;
        } else if (mode >= 8 && mode <= 11 && success == 1) { //협동모드 승리
            teamSuccess++;
        }

        double km = 1;
        switch (mode % 4) {
            case 1:
                km = 2;
                break;
            case 2:
                km = 3;
                break;
            case 3:
                km = 5;
                break;
        }

        double newPace = (km == 0 || elapsedTime == 0) ? 0 : (double) elapsedTime / km;

        if (originPace == 0) {
            averagePace = newPace;
        } else {
            averagePace = ((originPace * originCount) + newPace) / (originCount + 1);
        }
        record = new RecordDto(userId,record.getTotalCount()+1,
                record.getTotalKm()+km,record.getTotalTime()+elapsedTime,
                averagePace,teamSuccess,completeSuccess);
        recordMapper.updateRecord(record);

    }

    @Override
    public void addRaceInfo(RaceInfoReqDto raceInfoReqDto, MultipartFile poly) {
        //{"user_id":1, "race_id":999, "mode":2, "velocity":23, "elapsed_time":701,"average_heart_rate":150, "success":1}

        String url = this.getUrl(poly);

        int userId = raceInfoReqDto.getUserId();
        int mode = raceInfoReqDto.getMode();

        float km = 1;
        switch (mode % 4) {
            case 1:
                km = 2;
                break;
            case 2:
                km = 3;
                break;
            case 3:
                km = 5;
                break;
        }

        // 소모 칼로리
        // (10 × 몸무게) + (6.25 × 키) – (5 × 나이)
        UserDto user = userService.getUserById(userId);
        float w = user.getWeight();
        float h = user.getHeight();
        int age = user.getAge();
        int gender = user.getGender();
        float kcal = (float)((10*w)+(6.25*h)-(5*age) + 5);
        if(gender == 1) {
            kcal -= 166;
        }

        RaceInfoDto raceInfo = new RaceInfoDto();
        raceInfo.setUserId(userId);
        raceInfo.setRaceId(raceInfoReqDto.getRaceId());
        raceInfo.setVelocity(raceInfoReqDto.getVelocity());
        raceInfo.setSuccess(raceInfoReqDto.getSuccess());
        raceInfo.setHeartRate(raceInfoReqDto.getHeartRate());
        raceInfo.setTime(raceInfoReqDto.getTime());
        raceInfo.setDate(System.currentTimeMillis());
        raceInfo.setKm(km);
        raceInfo.setKcal(kcal);
        raceInfo.setPolyline(url);
        recordMapper.addRaceInfo(raceInfo);
    }


    private String getUrl(MultipartFile poly) {
        String url = "";
        if(poly != null && !poly.isEmpty()) {
            String [] formats = {".jpeg", ".png", ".bmp", ".jpg", ".PNG", ".JPEG"};
            // 원래 파일 이름 추출
            String origName = poly.getOriginalFilename();

            // 확장자 추출(ex : .png)
            String extension = origName.substring(origName.lastIndexOf("."));

            String folderName = "polyline";
            for(int i = 0; i < formats.length; i++) {
                if (extension.equals(formats[i])){
                    // user email과 확장자 결합
                    File uploadFile = null;
                    try {
                        uploadFile = osUpload.convert(poly)        // 파일 생성
                                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String fileName = folderName + "/" + System.nanoTime() + extension;
                    osUpload.put(bucketName, fileName, uploadFile);

                    url = "https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName;
                    break;
                }
            }
        }
        return url;
    }

    @Override
    public Map<String, Object> getRaceInfo(int userId, long startDate, long endDate) {
        Map<String, Object> result = new HashMap<>();

        // 레이스 정보 리스트 가져오기
        List<RaceModeInfoDto> raceList = recordMapper.getRaceInfo(userId, startDate, endDate);
        result.put("raceList", raceList);
        // 레이스 정보 요약 데이터 가져오기
        RaceInfoSummaryDto summaryData = recordMapper.getRaceInfoSummary(userId, startDate, endDate);
        result.put("summaryData", summaryData);

        return result;
    }

    @Override
    public RaceInfoSummaryDto getRaceInfoSummary(int userId, long startDate, long endDate) {
        RaceInfoSummaryDto data = recordMapper.getRaceInfoSummary(userId, startDate, endDate);
        return data;
    }



}
