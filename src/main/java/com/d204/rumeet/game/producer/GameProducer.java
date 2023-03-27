package com.d204.rumeet.game.producer;

import com.d204.rumeet.kafka.model.KafkaService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


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
    public String createTopic() {
        String newTopic = "rummet" + "." + "game-id" + "." + gameId + "." + "user-id" + "." + userId;

        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "j8d204.p.ssafy.io:9092");

        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic newUserATopic = new NewTopic(newTopic, numPartitions, replicationFactor);

            adminClient.createTopics(Collections.singleton(newUserATopic)).all().get();

            System.out.println("토픽 생성 완료: " + newTopic);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                System.out.println("토픽이 이미 존재합니다: " + newTopic);
            } else {
                System.err.println("토픽 생성 실패: " + newTopic);
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            System.err.println("토픽 생성 대기 중 예외 발생: " + newTopic);
            e.printStackTrace();
        }
        return newTopic;
    }
}

