package com.d204.rumeet.friend.model.dto;

import lombok.Data;

@Data
public class FriendListDto {
    int userId;
    String nickname;
    int pace;
    String profileImg;
    int matchCount;
    long latestDate;


}
