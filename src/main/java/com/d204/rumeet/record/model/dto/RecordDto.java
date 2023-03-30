package com.d204.rumeet.record.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordDto {
    int userId;
    int totalCount;
    double totalKm;
    long totalTime;
    double averagePace;
    int teamSuccessCount;
    int competitionSuccessCount;


}
