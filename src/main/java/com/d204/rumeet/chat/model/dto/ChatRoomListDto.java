package com.d204.rumeet.chat.model.dto;

import lombok.Data;

@Data
public class ChatRoomListDto {
    int roomId;
    int userId;
    int noReadCnt;
    String nickname;
    String profile;
    String content;
}
