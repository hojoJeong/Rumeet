package com.d204.rumeet.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    @RabbitListener(queues = "chat.queue")
    public String message(String msg) {
        System.out.println(msg);
        System.out.println(System.currentTimeMillis());
        return msg;
    }

}
