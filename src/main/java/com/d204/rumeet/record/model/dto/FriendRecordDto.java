package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class FriendRecordDto {
    int id;
    String nickname;
    String profileImg;
    double totalKm;
    long totalTime;
    int pace;

}
