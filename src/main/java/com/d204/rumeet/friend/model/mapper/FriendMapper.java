package com.d204.rumeet.friend.model.mapper;

import com.d204.rumeet.friend.model.dto.FriendListDto;
import com.d204.rumeet.friend.model.dto.FriendMatchDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper {

    int getMatchCount(int userId, int friendId);
    FriendListDto getNoRunningFriend(int userId);
    FriendListDto getRunningFriend(int userId, int friendId);

}
