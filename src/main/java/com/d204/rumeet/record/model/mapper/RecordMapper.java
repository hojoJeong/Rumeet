package com.d204.rumeet.record.model.mapper;

import com.d204.rumeet.record.model.dto.RaceInfoDto;
import com.d204.rumeet.record.model.dto.RecordDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper {

    RecordDto getRecord(int userId);
    void updateRecord(int userId, float pace, float km, long elapsedTime, int teamSuccess, int completeSuccess);
    void addRaceInfo(RaceInfoDto raceInfo);

}
