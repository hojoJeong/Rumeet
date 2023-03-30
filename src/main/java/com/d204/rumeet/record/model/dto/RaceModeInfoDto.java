package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class RaceModeInfoDto {

    int raceId;
    int userId;
    int mode;
    double velocity;
    float km;
    long time;
    int heartRate;
    double kcal;
    int success;
    String polyline;
    long date;
}
