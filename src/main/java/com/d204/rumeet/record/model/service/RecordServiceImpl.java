package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RecordDto;
import com.d204.rumeet.record.model.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordMapper recordMapper;


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

        int cnt = 0;
        for (int i = 1; i <= 5; i++) {
            String paceNo = "pace" + i;
            if (data.containsKey(paceNo)) {
                cnt++;
            }
        }

        float newPace = (cnt == 0 || elapsedTime == 0) ? 0 : (float) elapsedTime / cnt;

        if (originPace == 0) {
            averagePace = newPace;
        } else {
            averagePace = ((originPace * originCount) + newPace) / (originCount + 1);
        }
        float km = (float) cnt;
        recordMapper.updateRecord(userId, averagePace, km, elapsedTime, teamSuccess, completeSuccess);

    }

    @Override
    public void updateRaceInfo(String jsonData) throws org.json.simple.parser.ParseException {
        
    }



}
