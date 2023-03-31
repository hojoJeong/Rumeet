package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class RaceModeInfoDto {
    int raceId;
    int userId;
    int mode;
    long time;
    float km;
    int pace;
    int heartRate;
    double kcal;
    int success;
    String polyline;
    long date;
}
