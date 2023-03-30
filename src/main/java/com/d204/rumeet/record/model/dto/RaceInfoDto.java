package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class RaceInfoDto {

    int race_id;
    int user_id;
    float velocity;
    float km;
    long time;
    int heart_rate;
    float kcal;
    int success;
    long date;

}
