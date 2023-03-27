package com.d204.rumeet.game.producer;

import com.d204.rumeet.kafka.model.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TopicProducer {

    private KafkaService kafkaService;

    public String createUserTopic(int userId) {
        String userTopic = "user" + "." + userId;
        kafkaService.createTopic(userTopic);
        return userTopic;
    }

}
