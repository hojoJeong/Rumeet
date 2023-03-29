package com.d204.rumeet.chat.handler;

import com.d204.rumeet.chat.model.dto.ChatDto;
import com.d204.rumeet.chat.model.dto.ChatRoomDto;
import com.d204.rumeet.chat.model.dto.MakeChatRoomDto;
import com.d204.rumeet.chat.model.service.ChatService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatRoomHandler {

    private final ChatService chatService;
    //서버로 들어오는 채팅값
    @RabbitListener(queues = "chat.queue")
    public void chatControl(Message message) {
        // Json : String -> Object : ChatDto
        ChatDto chat = chatService.convertChat(message);
        //비즈니스 로직 (mongodb 저장, 채팅 보내기)
        chatService.saveChat(chat);
        chatService.doChat(chat, message);

    }

    @RabbitListener(queues = "room.queue")
    public void makeRoom(Message message) {
        String str = new String(message.getBody());
        MakeChatRoomDto dto = new Gson().fromJson(str, MakeChatRoomDto.class);
        ChatRoomDto chatRoomDto = chatService.makeRoom(dto);
        chatService.createQueue(chatRoomDto);
    }


}
