package com.d204.rumeet.record.model.service;

import com.d204.rumeet.record.model.dto.RaceInfoDto;
import com.d204.rumeet.record.model.dto.RaceInfoReqDto;
import com.d204.rumeet.record.model.dto.RaceModeInfoDto;
import com.d204.rumeet.record.model.dto.RecordDto;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {

    RecordDto getRecord(int userId);

    void updateRecord(RaceInfoReqDto data);

    void addRaceInfo(RaceInfoReqDto data, MultipartFile poly);

    List<RaceModeInfoDto> getRaceInfo(int userid);

}
