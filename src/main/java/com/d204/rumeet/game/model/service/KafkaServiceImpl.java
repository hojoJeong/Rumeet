package com.d204.rumeet.game.model.service;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.game.model.dto.GamePaceDto;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class KafkaServiceImpl implements KafkaService {

    private Properties props = this.setProps();

    @Override
    public void createTopic(String topicTitle) {
        int numPartitions = 1;
        short replicationFactor = 1;

        try (AdminClient adminClient = AdminClient.create(props)) {
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
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.printf("Produced record to topic %s, partition %d, offset %d%n", metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
        producer.close();
    }

    @Override
    public Properties setProps() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "j8d204.p.ssafy.io:9092");
        props.setProperty("group.id", "group-id");
        props.setProperty("key.serializer", StringSerializer.class.getName());
        props.setProperty("value.serializer", StringSerializer.class.getName());
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());
        return props;
    }

    @Override
    public GamePaceDto messageBYFastApi(int mode, int userId) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://j8d204.p.ssafy.io:8000/load/" + mode + "/" + userId).get().build();
        Call call = client.newCall(request);
        String responseBody= "";
        RespData<String> data = new RespData<>();
        try {
            Response response = call.execute();
            responseBody = response.body().string();
            System.out.println("responseBody = " + responseBody);
            data.setData(responseBody);
        } catch (IOException e) {
            System.out.println("e = " + e);
        }
        System.out.println(responseBody);
        GamePaceDto user = new Gson().fromJson(responseBody, GamePaceDto.class);

        return user;
    }

    @Override
    public boolean topicExists(String topicName) throws ExecutionException, InterruptedException {
        try (AdminClient adminClient = AdminClient.create(props)) {
            ListTopicsResult topics = adminClient.listTopics();
            List<String> topicNames = topics.listings().get().stream().map(TopicListing::name).collect(Collectors.toList());
            return topicNames.contains(topicName);
        }
    }

    @Override
    public void setMatching(int userAId, int userBId, String gameInfo) {
        String userATopic = "rumeet.user." + userAId;
        String userBTopic = "rumeet.user." + userBId;
        this.sendMessage(userATopic, gameInfo);
        this.sendMessage(userBTopic, gameInfo);
    }

}
