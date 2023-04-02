package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class RaceInfoReqDto {
    //{"userId":1, "raceId":999, "mode":2, "velocity":23, "time":701,"heartRate":150, "success":1}
    int userId;
    int raceId;
    int mode;
    double velocity;
    int time;
    int heartRate;
    int success;


}
