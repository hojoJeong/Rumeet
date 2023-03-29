package com.d204.rumeet.chat.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDataDto {
    int id;
    int user1;
    int user2;
    long date;
    int state;
    List<ChatDto> chat;
}
