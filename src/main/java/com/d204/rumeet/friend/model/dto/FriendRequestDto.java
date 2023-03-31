package com.d204.rumeet.friend.model.dto;

import lombok.Data;

@Data
public class FriendRequestDto {
    int fromUserId;
    int toUserId;

    String toUserName;
}
