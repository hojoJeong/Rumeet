package com.d204.rumeet.game.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendDto {
    int userId;
    String nickname;
    String profile;
}
