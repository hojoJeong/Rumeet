package com.d204.rumeet.kafka.model;

import java.util.Properties;

public interface KafkaService {

    void createTopic(String topicTitle);

    void sendMessage(String topic, String message);

    Properties setProps();

}