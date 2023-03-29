package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RecordDto;
import com.d204.rumeet.record.model.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
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
    public void updateRecord(String json_data) throws org.json.simple.parser.ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject data = (JSONObject) jsonParser.parse(json_data);

        int userId = Integer.parseInt(data.get("user_id").toString());

        RecordDto origin_record = recordMapper.getRecord(userId);
        Integer origin_pace = origin_record.getAverage_pace();
        int origin_count = origin_record.getTotal_count();
        float average_pace;

        int km = 0;
        int total = 0;
        for (int i = 1; i <= 5; i++) {
            String paceNo = "pace" + i;
            if (data.containsKey(paceNo)) {
                km++;
                total += Integer.parseInt(data.get(paceNo).toString());
            }
        }
        float new_pace = (float) total / km;
        if (origin_pace == null) {
            average_pace = new_pace;
        } else {
            average_pace = ((origin_pace * origin_count) + new_pace) / (origin_count + 1);
        }

        recordMapper.updateRecord(userId, average_pace, km);

    }


}
