package com.d204.rumeet.chat.model.mapper;

import com.d204.rumeet.chat.model.dto.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    void makeRoom(ChatRoomDto chatDto);

    ChatRoomDto getChatRoom(int userId);
}
