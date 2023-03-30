package com.d204.rumeet.fcm.model.mapper;

import com.d204.rumeet.fcm.model.dto.FcmTokenDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FcmTokenMapper {
    int updateUserToken(FcmTokenDto fcmTokenDto);
}
