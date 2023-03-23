package com.d204.rumeet.game.topic;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MatchingTopic {
    @KafkaListener(topics = "rumeet.matching", groupId = "matching")
    public void listenGroupA(String message) {
        System.out.println("matching" + " : " + message);
    }
}
