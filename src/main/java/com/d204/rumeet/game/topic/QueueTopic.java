package com.d204.rumeet.game.topic;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QueueTopic {
    @KafkaListener(topics = "rumeet.startqueue", groupId = "startqueue")
    public void listenGroupA(String message) {
        System.out.println("startqueue" + " : " + message);
    }
}
