package com.d204.rumeet.game.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchAcceptDto {
    int userId;
    int roomId;
}
