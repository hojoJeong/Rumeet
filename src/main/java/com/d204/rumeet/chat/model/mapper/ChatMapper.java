package com.d204.rumeet.chat.model.mapper;

import com.d204.rumeet.chat.model.dto.ChatRoomDto;
import com.d204.rumeet.chat.model.dto.CreateChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    void makeRoom(ChatRoomDto chatDto);

    ChatRoomDto getChatRoom(int userId);
    ChatRoomDto findChatRoom(CreateChatRoomDto dto);

    void deleteRoomById(int id);

    ChatRoomDto getRoomById(int id);
}
