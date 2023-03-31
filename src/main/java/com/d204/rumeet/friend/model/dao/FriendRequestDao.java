package com.d204.rumeet.friend.model.dao;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "friendRequest")
@ToString
public class FriendRequestDao {

    @Id
    private String id;
    private Integer fromUserId;
    private Integer toUserId;

    private String toUserName;
    private Long date;

    @Builder
    public FriendRequestDao(String id, Integer fromUserId, Integer toUserId, String toUserName, Long date) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.date = date;
    }

}
