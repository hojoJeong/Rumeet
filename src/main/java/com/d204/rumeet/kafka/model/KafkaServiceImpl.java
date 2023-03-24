package com.d204.rumeet.kafka.model;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaServiceImpl implements KafkaService {
    @Override
    public void createTopic(String topicTitle) {
        int numPartitions = 1;
        short replicationFactor = 1;

        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic newUserATopic = new NewTopic(topicTitle, numPartitions, replicationFactor);

            adminClient.createTopics(Collections.singleton(newUserATopic)).all().get();

            System.out.println("토픽 생성 완료: " + topicTitle);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                System.out.println("토픽이 이미 존재합니다: " + topicTitle);
            } else {
                System.err.println("토픽 생성 실패: " + topicTitle);
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            System.err.println("토픽 생성 대기 중 예외 발생: " + topicTitle);
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String topic, String message) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 메시지 생성 및 produce
        String value = message;
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.printf("Produced record to topic %s, partition %d, offset %d%n", metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
        System.out.println(producer);
        // Producer 종료
        producer.close();
    }
}
