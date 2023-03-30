package com.d204.rumeet.fcm.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FcmTokenDto {
    int userId;
    String fcmToken;
}
