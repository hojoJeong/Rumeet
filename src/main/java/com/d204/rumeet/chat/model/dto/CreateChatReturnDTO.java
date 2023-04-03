package com.d204.rumeet.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatReturnDTO {
    int roomId;
    int userId;
    String nickname;
    String profile;
    int noReadCnt;
}
