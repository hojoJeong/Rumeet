package com.d204.rumeet.chat.handler;

import com.d204.rumeet.chat.model.dto.ChatDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatRoomHandler {

    @RabbitListener(queues = "chat.queue.2")
    public void message(Message message) {
        String str = new String(message.getBody());
        ChatDto chat = new Gson().fromJson(str,ChatDto.class);
        chat.setDate(System.currentTimeMillis());
        log.info("{}",chat);
    }

}
