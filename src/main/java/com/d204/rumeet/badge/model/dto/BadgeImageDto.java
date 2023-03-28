package com.d204.rumeet.badge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BadgeImageDto {
    int id;
    String name;
    String description;
    int type;

    long date;
    String badge_img;
    String badge_img_black;
    int flag;
}
