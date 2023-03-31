package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.*;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {

    RecordDto getRecord(int userId);

    void updateRecord(RaceInfoReqDto data);

    void addRaceInfo(RaceInfoReqDto data, MultipartFile poly);

    List<RaceModeInfoDto> getRaceInfo(int userid);

    RaceInfoSummaryDto getRaceInfoSummary(int userId, long startDate, long endDate);

}
