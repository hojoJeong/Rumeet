package com.d204.rumeet.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyUserDto {
    int id;
    int gender;
    int age;
    float height;
    float weight;
}
