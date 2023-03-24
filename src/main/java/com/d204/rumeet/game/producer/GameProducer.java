package com.d204.rumeet.game.producer;

import com.d204.rumeet.kafka.model.KafkaService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GameProducer {
    private String bootstrapServers;
    private int gameId;
    private int userId;
    int numPartitions = 1; // 파티션의 수
    short replicationFactor = 1; // 복제 수
    private KafkaService kafkaService;

    public GameProducer(String bootstrapServers, int gameId, int userId) {
        this.bootstrapServers = bootstrapServers;
        this.gameId = gameId;
        this.userId = userId;
    }

    public String createGameTopic() {
        String gameTopic = "rummet" + "." + "game-id" + "." + gameId + "." + "user-id" + "." + userId;
        kafkaService.createTopic(gameTopic);
        return gameTopic;
    }
}

