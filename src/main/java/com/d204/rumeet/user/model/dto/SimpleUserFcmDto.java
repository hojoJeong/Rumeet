package com.d204.rumeet.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleUserFcmDto {
    int id;
    String nickname;
    String fcmToken;
    int friendAlarm;
    int matchingAlarm;

}
