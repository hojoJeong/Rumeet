package com.d204.rumeet.user.model.dto;

import lombok.Data;

@Data
public class UserRecordDto {
    int id;
    String nickname;
    String profileImg;
    double totalKm;
    long totalTime;
    int pace;
}
