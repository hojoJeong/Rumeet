package com.d204.rumeet.record.model.dto;


import lombok.Data;

@Data
public class RecordDto {
    int user_id;
    int total_count;
    float total_km;
    long total_time;
    float average_pace;

    int team_success_count;
    int competition_success_count;




}
