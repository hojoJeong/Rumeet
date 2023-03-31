package com.d204.rumeet.friend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendMatchDto {
    int userId;
    int friendId;
    String friendName;
    int count;
    long latestDate;
}
