package com.d204.rumeet.record.model.dto;

import lombok.Data;

@Data
public class MatchInfoSummaryDto {
    int userId;
    int matchCount;
    int success;
    int fail;

}
