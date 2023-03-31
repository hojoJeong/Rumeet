package com.d204.rumeet.record.model.mapper;

import com.d204.rumeet.record.model.dto.RaceInfoDto;
import com.d204.rumeet.record.model.dto.RaceInfoSummaryDto;
import com.d204.rumeet.record.model.dto.RaceModeInfoDto;
import com.d204.rumeet.record.model.dto.RecordDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordMapper {

    RecordDto getRecord(int userId);
    void updateRecord(RecordDto record);
    void addRaceInfo(RaceInfoDto raceInfo);
    List<RaceModeInfoDto> getRaceInfo(int userId, long startDate, long endDate);

    RaceInfoSummaryDto getRaceInfoSummary(int userId, long startDate, long endDate);

}
