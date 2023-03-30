package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RaceInfoDto;
import com.d204.rumeet.record.model.dto.RecordDto;
import org.apache.tomcat.util.json.ParseException;

import java.util.List;

public interface RecordService {

    RecordDto getRecord(int userId);

    void updateRecord(String data) throws ParseException, org.json.simple.parser.ParseException;

    void addRaceInfo(String data) throws ParseException, org.json.simple.parser.ParseException;

    List<RaceInfoDto> getRaceInfo(int userid);

}
