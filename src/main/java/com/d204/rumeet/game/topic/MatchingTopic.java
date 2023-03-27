package com.d204.rumeet.game.topic;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MatchingTopic {
    @KafkaListener(topics = "rumeet.matching.1", groupId = "matching")
    public void listenMatching1(String message) {
        System.out.println("matching.1" + " : " + message);
    }

    @KafkaListener(topics = "rumeet.matching.2", groupId = "matching")
    public void listenMatching2(String message) {
        System.out.println("matching.2" + " : " + message);
    }

    @KafkaListener(topics = "rumeet.matching.3", groupId = "matching")
    public void listenMatching3(String message) {
        System.out.println("matching.3" + " : " + message);
    }
}
