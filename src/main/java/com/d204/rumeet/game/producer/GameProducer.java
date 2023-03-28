package com.d204.rumeet.game.producer;

import com.d204.rumeet.kafka.model.KafkaService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GameProducer {
    private final int gameId;
    private final int userId;
    private final KafkaService kafkaService;

    public String createGameTopic() {
        String gameTopic = "rummet" + "." + "game-id" + "." + gameId + "." + "user-id" + "." + userId;
        kafkaService.createTopic(gameTopic);
        return gameTopic;
    }
}

