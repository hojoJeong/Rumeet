package com.d204.rumeet.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyAlarmStateDto {
    int userId;
    int target;
    int state;
}
