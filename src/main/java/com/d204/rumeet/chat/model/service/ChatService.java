package com.d204.rumeet.chat.model.service;

import com.d204.rumeet.chat.model.dto.ChatDto;
import com.d204.rumeet.chat.model.dto.ChatRoomDto;
import com.d204.rumeet.chat.model.dto.ChatRoomListDto;
import com.d204.rumeet.chat.model.dto.MakeChatRoomDto;
import org.springframework.amqp.core.Message;

import java.util.List;

public interface ChatService {
    ChatRoomDto makeRoom(MakeChatRoomDto makeChatRoomDto);

    ChatDto convertChat(Message message);

    void saveChat(ChatDto chat);

    void doChat(ChatDto chat, Message message);

    ChatRoomDto getChatRoom(int userId);

    List<ChatDto> getChatByRoomId(int roomId);

    void createQueue(ChatRoomDto chatRoomDto);

    List<ChatRoomListDto> getChatRoomList(int userId);
}
