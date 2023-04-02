package com.d204.rumeet.friend.model.dto;

import lombok.Data;

@Data
public class FriendRequestInfoDto {
    private String id;
    private Integer fromUserId;
    private Integer toUserId;

    private String fromUserName;
    private String fromUserProfile;
    private Long date;

}
