package com.d204.rumeet.game.model.dto;

import lombok.Data;

@Data
public class RaceDto {
    int id;
    int userId;
    int partnerId;
    int mode;
    long date;
}
