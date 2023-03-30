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
        //{"user_id":1,"pace1":192, "pace2":241, "pace3":268}
        JSONParser jsonParser = new JSONParser();
        JSONObject data = (JSONObject) jsonParser.parse(jsonData);

        int userId = Integer.parseInt(data.get("user_id").toString());

        RecordDto originRecord = recordMapper.getRecord(userId);
        Float originPace = originRecord.getAverage_pace();
        Integer originCount = originRecord.getTotal_count();
        float averagePace;

        int cnt = 0;
        int total = 0;
        for (int i = 1; i <= 5; i++) {
            String paceNo = "pace" + i;
            if (data.containsKey(paceNo)) {
                cnt++;
                total += Integer.parseInt(data.get(paceNo).toString());
            }
        }

        float newPace = (cnt == 0 || total == 0) ? 0 : (float) total / cnt;

        if (originPace == 0) {
            averagePace = newPace;
        } else {
            averagePace = ((originPace * originCount) + newPace) / (originCount + 1);
        }
        float km = (float) cnt;
        recordMapper.updateRecord(userId, averagePace, km);

    }



}
