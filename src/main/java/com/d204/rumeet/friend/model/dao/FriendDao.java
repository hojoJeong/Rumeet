package com.d204.rumeet.friend.model.dao;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "friend")
@ToString
public class FriendDao {
    private Integer userId;
    private Integer friendId;
    private Long date;

    @Builder
    public FriendDao(Integer userId, Integer friendId, Long date) {
        this.userId = userId;
        this.friendId = friendId;
        this.date = date;
    }


}