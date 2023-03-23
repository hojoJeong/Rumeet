package com.d204.rumeet.chat.model.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "LastChatDto")
@Data
public class LastChatDto {
    int roomId;
    int toUserId;
    int fromUserId;
    String content;
    long date;
}
