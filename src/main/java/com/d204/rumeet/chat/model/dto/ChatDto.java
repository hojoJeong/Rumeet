package com.d204.rumeet.chat.model.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "chat")
@Data
public class ChatDto {
    int roomId;
    int toUserId;
    int fromUserId;
    String content;
    long date;
}
