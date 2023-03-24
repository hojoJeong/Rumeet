package com.d204.rumeet.kafka.model;

public interface KafkaService {

    void createTopic(String topicTitle);

    void sendMessage(String topic, String message);
}
