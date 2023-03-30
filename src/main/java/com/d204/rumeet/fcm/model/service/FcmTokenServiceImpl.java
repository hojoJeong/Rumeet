package com.d204.rumeet.fcm.model.service;

import com.d204.rumeet.fcm.model.dto.FcmTokenDto;
import com.d204.rumeet.fcm.model.mapper.FcmTokenMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmTokenServiceImpl implements FcmTokenService {
    private final FcmTokenMapper fcmTokenMapper;
    @Override
    public int updateUserToken(FcmTokenDto fcmTokenDto) {
        return fcmTokenMapper.updateUserToken(fcmTokenDto);
    }
}
