package com.d204.rumeet.friend.model.mapper;

import com.d204.rumeet.friend.model.dto.FriendMatchDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper {

    FriendMatchDto getFriendMatch(int userId, int friendId);

}
