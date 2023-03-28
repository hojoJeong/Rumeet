package com.d204.rumeet.badge.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class BadgeDto {
    String name;
    String description;
    int type;
}
