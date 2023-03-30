package com.d204.rumeet.record.model.mapper;

import com.d204.rumeet.record.model.dto.RecordDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper {

    void updateRecord(int userId, float pace, float km, long time);

    RecordDto getRecord(int userId);
}
