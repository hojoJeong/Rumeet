package com.d204.rumeet.kafka.model.consumer;

import com.d204.rumeet.game.model.service.KafkaService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.*;

//@Component
@RequiredArgsConstructor
public class MatchingConsumer {

    @Autowired
    private final KafkaService kafkaService;
    private List<String> messages = Collections.synchronizedList(new ArrayList<String>());

    @PostConstruct
    public void init() {
        listenMatching();
    }

    public void listenMatching() {
        String gameMathcingTopic = "rumeet.game.matching";

        Properties props = kafkaService.setProps();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

            consumer.subscribe(Arrays.asList(gameMathcingTopic));
            System.out.println("consumer = " + consumer);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(records.count());
                    messages.add(record.value());

                    System.out.println("messages = " + messages);
                    System.out.println("Received messages:");

                    for (String message : messages) {
                        // 1. 매칭할 인원 분류
                        // userAId, userBId, gameInfo
                        // 2. 각 유저 토픽에게 게임 정보 전달
                        // kafkaService.setMatching(userA, userB, agmeInfo);
                    }
                }
            }
        }
    }
}