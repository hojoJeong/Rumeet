package com.d204.rumeet.kafka.model.service;

import com.d204.rumeet.game.model.dto.GamePaceDto;
import org.springframework.http.ResponseEntity;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public interface KafkaService {

    void createTopic(String topicTitle);

    void sendMessage(String topic, String message);

    Properties setProps();

    GamePaceDto messageBYFastApi(int mode, int userId);

    boolean topicExists(String topicName) throws ExecutionException, InterruptedException;

    void setMatching(int userAId, int userBId, String gameInfo);
}