package com.d204.rumeet.game.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendRaceInfoDto {
    int raceId;
    int userId;
    int partnerId;
    String nickname;
    String profile;
    int mode;
    long date;
    int state;

}
