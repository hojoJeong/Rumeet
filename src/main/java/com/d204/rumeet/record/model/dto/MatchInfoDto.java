package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class MatchInfoDto {
    int raceId;
    int userId;
    int mode;
    int success;
    long date;
    String partnerName;
    int pace;
    int time;
    double km;
    int heartRate;
    double kcal;
    String polyline;

}
