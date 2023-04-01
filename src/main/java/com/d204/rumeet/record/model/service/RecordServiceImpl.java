package com.d204.rumeet.record.model.service;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.service.BadgeService;
import com.d204.rumeet.record.model.dto.*;
import com.d204.rumeet.record.model.mapper.RecordMapper;
import com.d204.rumeet.tools.OSUpload;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
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

    private final BadgeService badgeService;



    final String bucketName = "rumeet";
    private final OSUpload osUpload;
    @Override
    public RecordDto getRecord(int userId) {
        RecordDto record = recordMapper.getRecord(userId);
        return record;
    }

    @Override
    public Map<String, Object> getMainRecord(int userId) {
        Map<String, Object> result = new HashMap<>();

        MainRecordDto record = recordMapper.getMainRecord(userId);
        result.put("record", record);
        List<BadgeDto> badge = badgeService.getRecentBadgesByUserId(userId);
        result.put("badge", badge);
        return result;
    }

    @Override
    public void updateRecord(RaceInfoReqDto raceInfoReqDto)  {

        //{"userId":1, "raceId":999, "mode":2, "velocity":23, "time":701,"heartRate":150, "success":1}
        System.out.println("들어온거"+raceInfoReqDto);

        int mode = raceInfoReqDto.getMode();
        int userId = raceInfoReqDto.getUserId();
        int success = raceInfoReqDto.getSuccess();
        int elapsedTime = raceInfoReqDto.getTime();
        // 기존 정보
        RecordDto record = recordMapper.getRecord(userId);
        System.out.println("기존"+record);

        int originPace = record.getAveragePace();
        int originCount = record.getTotalCount();
        int matchCount = record.getMatchCount();
        int completeSuccess = record.getCompetitionSuccessCount();
        int teamSuccess = record.getTeamSuccessCount();
        int averagePace;

        if (mode >= 4) {
            matchCount++;
            if (success == 1) {
                if (mode >= 4 && mode <= 7) { // 경쟁모드 승리
                    completeSuccess++;
                } else if (mode >= 8 && mode <= 11) { // 협동모드 승리
                    teamSuccess++;
                }
            }
        }


        int km = 1;
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

        int newPace = (km == 0 || elapsedTime == 0) ? 0 : (int) elapsedTime / km;

        if (originPace == 0) {
            averagePace = newPace;
        } else {
            averagePace = ((originPace * originCount) + newPace) / (originCount + 1);
        }

        record = new RecordDto(userId,record.getTotalCount()+1,
                record.getTotalKm()+km,record.getTotalTime()+elapsedTime,
                averagePace, matchCount, teamSuccess,completeSuccess);
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
    public Map<String, Object> getMatchInfo(int userId){
        Map<String, Object> result = new HashMap<>();
        // 요약 정보
        MatchInfoSummaryDto summaryData = recordMapper.getMatchInfoSummary(userId);
        result.put("summaryData",summaryData);
        // 매칭 레이스 정보
        List<MatchInfoDto> raceList = recordMapper.getMatchInfo(userId);
        result.put("raceList", raceList);
        return result;
    }
}
