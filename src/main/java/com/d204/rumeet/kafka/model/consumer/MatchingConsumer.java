package com.d204.rumeet.kafka.model.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MatchingConsumer {
    // 일번
    // game stsart 시키는 consumer
    // matching topic 0 ~ 11 구독
    @KafkaListener(topicPattern = "rumeet\\.game\\.matching\\..*", groupId = "*")
    public void listenMatching(String message) {
        System.out.println("message = " + message);
    }
}