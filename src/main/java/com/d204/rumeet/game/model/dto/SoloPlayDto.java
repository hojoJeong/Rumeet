package com.d204.rumeet.game.model.dto;

import lombok.Data;

@Data
public class SoloPlayDto {
    int id;
    int userId;
    int partnerId;
    int mode;
    long date;
    int state;
    int[] pace;
}
