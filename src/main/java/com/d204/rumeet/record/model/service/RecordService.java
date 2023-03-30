package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RecordDto;
import org.apache.tomcat.util.json.ParseException;

public interface RecordService {

    RecordDto getRecord(int userId);

    void updateRecord(String data) throws ParseException, org.json.simple.parser.ParseException;

    void updateRaceInfo(String data) throws ParseException, org.json.simple.parser.ParseException;

}
