package com.d204.rumeet.game.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TopicProducer {

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
