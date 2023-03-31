package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class RaceModeInfoDto {
    int raceId;
    int userId;
    int mode;
    float avgPace;
    int heartRate;
    double kcal;
    int success;
    String polyline;
    long date;
}
