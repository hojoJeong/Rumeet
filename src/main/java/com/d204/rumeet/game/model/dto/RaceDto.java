package com.d204.rumeet.game.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RaceDto {
    int id;
    int userId;
    int partnerId;
    int mode;
    long date;
    int state;
}
