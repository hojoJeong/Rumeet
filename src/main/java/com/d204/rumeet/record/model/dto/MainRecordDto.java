package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class MainRecordDto {
    int userId;
    String nickname;
    int averagePace;
    int totalCount;
    double totalKm;
}
