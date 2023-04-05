package com.d204.rumeet.record.model.service;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.dto.MyBadgeDto;
import com.d204.rumeet.badge.model.mapper.BadgeMapper;
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
    private final BadgeMapper badgeMapper;
    private final UserService userService;
    private final BadgeService badgeService;

    final String bucketName = "rumeet";
    private final OSUpload osUpload;
    @Override
    public FriendRecordDto getFriendRecord(int userId) {
        FriendRecordDto record = recordMapper.getFriendRecord(userId);
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
    public void updateRecord(RaceInfoReqDto raceInfoReqDto) {

        int mode = raceInfoReqDto.getMode();
        int userId = raceInfoReqDto.getUserId();
        int success = raceInfoReqDto.getSuccess();
        int elapsedTime = raceInfoReqDto.getTime();

        // 레코드 추가
        RecordDto record = recordMapper.getRecord(userId);

        int originPace = record.getAveragePace();
        int originCount = record.getTotalCount();
        int matchCount = record.getMatchCount();
        int completeSuccess = record.getCompetitionSuccessCount();
        int teamSuccess = record.getTeamSuccessCount();

        if (mode >= 4) {
            matchCount++;
            if (success == 1) {
                if (mode >= 4 && mode <= 7) { // 경쟁모드 승리
                    completeSuccess++;
                } else if (mode >= 8 && mode <= 19) { // 협동모드 승리
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

        int newPace;
        int averagePace;
        if (success == 1) {
            newPace = (km == 0 || elapsedTime == 0) ? 0 : elapsedTime / km;
            if (originPace == 0) {
                averagePace = newPace;
            } else {
                averagePace = ((originPace * originCount) + newPace) / (originCount + 1);
            }
        } else {
            averagePace = originPace;
        }

        record = new RecordDto(userId, record.getTotalCount() + 1,
                record.getTotalKm() + km, record.getTotalTime() + elapsedTime,
                averagePace, matchCount, teamSuccess, completeSuccess);
        recordMapper.updateRecord(record);

        // 뱃지 추가
        double totalKm = record.getTotalKm();
        long totalTime = record.getTotalTime();
        long date = System.currentTimeMillis();

        // 경쟁 뱃지
        int[] competitionCounts = {30, 20, 10};
        int[] competitionBadges = {3, 2, 1};

        if (completeSuccess == 1) {
            MyBadgeDto myBadge = new MyBadgeDto(userId, 7, date); //경쟁 첫번째 승리
            badgeMapper.addBadge(myBadge);
        } else {
            for (int i = 0; i < competitionCounts.length; i++) {
                if (completeSuccess >= competitionCounts[i]) {
                    MyBadgeDto myBadge = new MyBadgeDto(userId, competitionBadges[i], date);
                    badgeMapper.addBadge(myBadge);
                    break;
                }
            }
        }

        // 협동 뱃지
        int[] teamCounts = {30, 20, 10};
        int[] teamBadges = {16, 15, 14};
        for (int i = 0; i < teamCounts.length; i++) {
            if (teamSuccess >= teamCounts[i]) {
                MyBadgeDto myBadge = new MyBadgeDto(userId, teamBadges[i], date);
                badgeMapper.addBadge(myBadge);
                break;
            }
        }

        // km 뱃지
        int[] kmGoals = {1000, 500, 100};
        int[] kmBadges = {5, 6, 4};
        for (int i = 0; i < kmGoals.length; i++) {
            if (totalKm >= kmGoals[i]) {
                MyBadgeDto myBadge = new MyBadgeDto(userId, kmBadges[i], date);
                badgeMapper.addBadge(myBadge);
                break;
            }
        }
        // time 뱃지
        int[] timeGoals = {180000, 72000, 36000}; //50시간, 20시간, 10시간
        int[] timeBadges = {10, 9, 8};
        for (int i = 0; i < timeGoals.length; i++) {
            if (totalTime >= timeGoals[i]) {
                MyBadgeDto myBadge = new MyBadgeDto(userId, timeBadges[i], date);
                badgeMapper.addBadge(myBadge);
                break;
            }
        }

    }

    @Override
    public void addRaceInfo(RaceInfoReqDto raceInfoReqDto) {
        //{"user_id":1, "race_id":999, "mode":2, "velocity":23, "elapsed_time":701,"average_heart_rate":150, "success":1}

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
        raceInfo.setPolyline(raceInfoReqDto.getPolyline());
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
