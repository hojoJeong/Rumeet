package com.d204.rumeet.fcm.model.service;

import com.d204.rumeet.fcm.model.dto.FcmTokenDto;

public interface FcmTokenService {
    int updateUserToken(FcmTokenDto fcmTokenDto);

    //int registerUserToken(FcmTokenDto fcmTokenDto) throws Exception;

}
