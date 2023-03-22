package com.d204.rumeet.chat.handler;

import com.d204.rumeet.chat.model.dto.ChatDto;
import com.d204.rumeet.chat.model.dto.ChatRoomDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatRoomHandler {

    private final RabbitTemplate rabbitTemplate;
    //서버로 들어오는 채팅값
    @RabbitListener(queues = "chat.queue")
    public void message(Message message) {
        String str = new String(message.getBody());
        ChatDto chat = new Gson().fromJson(str,ChatDto.class);
        chat.setDate(System.currentTimeMillis());
        log.info("{}", chat);

        //비즈니스 로직 추가 (mongodb 저장)

        //상대방에게 그대로 다시 전달하는 부분
        StringBuilder sb = new StringBuilder();
        sb.append("room.").append(chat.getRoomId()).append(".").append(chat.getToUserId());
        rabbitTemplate.convertAndSend("amq.topic",sb.toString(),chat);

    }

//    @RabbitListener(queues = "room.queue")
//    public void makeRoom(Message message) {
//        String str = new String(message.getBody());
//        ChatRoomDto chatRoomDto = new Gson().fromJson(str, ChatRoomDto.class);
//        chatRoomDto.setDate(System.currentTimeMillis());
//
//    }


}
