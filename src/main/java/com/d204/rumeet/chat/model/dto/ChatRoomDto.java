package com.d204.rumeet.chat.model.dto;

import lombok.Data;

@Data
public class ChatRoomDto {
    int id;
    int toUserId;
    int fromUserId;
    long date;
}
