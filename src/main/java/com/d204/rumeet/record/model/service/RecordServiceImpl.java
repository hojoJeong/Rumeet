package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RaceInfoDto;
import com.d204.rumeet.record.model.dto.RecordDto;
import com.d204.rumeet.record.model.mapper.RecordMapper;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordMapper recordMapper;
    private final UserService userService;

    @Override
    public RecordDto getRecord(int userId) {
        RecordDto record = recordMapper.getRecord(userId);
        return record;
    }

    @Override
    public void updateRecord(String jsonData) throws org.json.simple.parser.ParseException {

        //{"user_id":1, "pace1":192, "pace2":241 , "pace3":268 , "mode":2, "success":1, "elapsed_time":1234}
        JSONParser jsonParser = new JSONParser();
        JSONObject data = (JSONObject) jsonParser.parse(jsonData);

        int userId = Integer.parseInt(data.get("user_id").toString());
        int mode = Integer.parseInt(data.get("mode").toString());
        int success = Integer.parseInt(data.get("success").toString());
        int elapsedTime = Integer.parseInt(data.get("elapsed_time").toString());

        // 기존 정보
        RecordDto originRecord = recordMapper.getRecord(userId);
        float originPace = originRecord.getAverage_pace();
        int originCount = originRecord.getTotal_count();
        int completeSuccess = originRecord.getCompetition_success_count();
        int teamSuccess = originRecord.getTeam_success_count();
        float averagePace;

        if (mode >= 4 && mode <= 7 && success == 1) { //경쟁모드 승리
            completeSuccess++;
        } else if (mode >= 8 && mode <= 11 && success == 1) { //협동모드 승리
            teamSuccess++;
        }

        float km = 1;
        switch (mode % 4) {
            case 0:
                break;
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

        float newPace = (km == 0 || elapsedTime == 0) ? 0 : (float) elapsedTime / km;

        if (originPace == 0) {
            averagePace = newPace;
        } else {
            averagePace = ((originPace * originCount) + newPace) / (originCount + 1);
        }
        recordMapper.updateRecord(userId, averagePace, km, elapsedTime, teamSuccess, completeSuccess);

    }

    @Override
    public void addRaceInfo(String jsonData) throws org.json.simple.parser.ParseException {
        //{"user_id":1, "race_id":999, "mode":2, "velocity":23, "elapsed_time":701,"average_heart_rate":150, "success":1}
        JSONParser jsonParser = new JSONParser();
        JSONObject data = (JSONObject) jsonParser.parse(jsonData);

        int userId = Integer.parseInt(data.get("user_id").toString());
        int mode = Integer.parseInt(data.get("mode").toString());

        float km = 1;
        switch (mode % 4) {
            case 0:
                break;
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
        raceInfo.setUser_id(userId);
        raceInfo.setRace_id(Integer.parseInt(data.get("race_id").toString()));
        raceInfo.setVelocity(Float.parseFloat(data.get("velocity").toString()));
        raceInfo.setSuccess(Integer.parseInt(data.get("success").toString()));
        raceInfo.setHeart_rate(Integer.parseInt(data.get("average_heart_rate").toString()));
        raceInfo.setTime(Integer.parseInt(data.get("elapsed_time").toString()));
        raceInfo.setDate(System.currentTimeMillis());
        raceInfo.setKm(km);
        raceInfo.setKcal(kcal);

        recordMapper.addRaceInfo(raceInfo);

    }

    @Override
    public RaceInfoDto getRaceInfo(int userId) {
        RaceInfoDto race = recordMapper.getRaceInfo(userId);
        return race;
    }


}
