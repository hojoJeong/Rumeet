package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RecordDto;
import com.d204.rumeet.record.model.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.shaded.net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
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
    public void updateRecord(String data) throws ParseException {
        String data_tmp = "{\"user_id\":32,\"race_id\":36,\"pace1\":590,\"pace2\":460,\"elapsed_time\":1050,\"average_heart_rate\":140}";

        int userId = 0;
        RecordDto record = recordMapper.getRecord(userId);
//페이스 이미 있는지없는지에따라 다름


        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(data_tmp);



        //total_km, total_count, average_pace
        //{"user_id":32,"race_id":36,"pace1":590,"pace2":460,"elapsed_time":1050,"average_heart_rate":140}
        //{"user_id":32,"race_id":36,"pace1":590,"pace2":460,"pace3":600,"elapsed_time":1050,"average_heart_rate":140}

        // getRecord?
        int pace1 = 1234;
        int pace2 = 666;
        int pace3 = 500;
        userId = 1;
        int pace = 600;
        int km = 3;
        recordMapper.updateRecord(userId, pace, km);

    }


}
