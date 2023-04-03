package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.*;
import org.apache.ivy.Main;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RecordService {

    FriendRecordDto getFriendRecord(int userId);

    Map<String, Object> getMainRecord(int userId);
    void updateRecord(RaceInfoReqDto data);

    void addRaceInfo(RaceInfoReqDto data, MultipartFile poly);

    Map<String, Object> getRaceInfo(int userId, long startDate, long endDate);

    Map<String, Object> getMatchInfo(int userId);




}
