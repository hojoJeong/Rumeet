package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class RaceInfoDto {

    int raceId;
    int userId;
    double velocity;
    double km;
    long time;
    int heartRate;
    double kcal;
    int success;
    String polyline;
    long date;

}
