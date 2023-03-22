package com.d204.rumeet.chat.model.dto;

import lombok.Data;

@Data
public class ChatDto {
    int roomId;
    int toUserId;
    int fromUserId;
    String content;
    long date;
}
