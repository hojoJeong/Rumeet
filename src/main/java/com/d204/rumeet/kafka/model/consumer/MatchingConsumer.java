package com.d204.rumeet.kafka.model.consumer;

import com.d204.rumeet.kafka.model.KafkaService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class MatchingConsumer {
    private List<String> messages = Collections.synchronizedList(new ArrayList<String>());
    private final KafkaService kafkaService;

    @PostConstruct
    public void init() {
        listenMatching();
    }

    public void listenMatching() {
        Properties props = kafkaService.setProps();


        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList("rumeet.game.matching"));
            System.out.println("consumer = " + consumer);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(records.count()    );
                    messages.add(record.value());

                    System.out.println("messages = " + messages);
                    System.out.println("Received messages:");

                    for (String message : messages) {
//                        System.out.println("message = " + message);


//                        FAST Api Call Method
                        // String[] gameInfo = message.split(",");
                        // String mode = gameInfo[0];
                        // String userId = gameInfo[1];
                        // System.out.println(mode + userId);

//                        OkHttpClient client = new OkHttpClient();
//                        Request request = new Request.Builder()
//                                .url("http://j8d204.ssafy.io:8000/load/" + mode + "/" + userId)
//                                .get()
//                                .build();
//                        Call call = client.newCall(request);
//                        Response response = null;
//                        try {
//                            response = call.execute();
//                            System.out.println("response = " + response);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
                }
            }
        }
    }
}

// matching Topic 은 하나로하자
// 해당 토픽을 추적하면서 유저id 가 담긴 메시지 확인
// Q 에 집어기 전에 fastAPI 로 해당 유저 정보 받아온다
// 게임 매칭이 됐을 때 > 해당 유저한테 메시지만 보내주면 됨
// 유저들이 게임 신청을 할 때 그걸 서버가 구독하고 있어야함
// 게임 정보만 넣어주자

// 유저가 게임 시작을 요청하는건 클라이언트 -> 카프카

// 해야할거
// 1. 서버는 매칭 토픽을 구독하다가 해당 토픽에 메시지가 2개 가 됐을 때
// 각 유저(메시지 대상)에게 게임 정보를 전송함
// 2.


// int[] 매칭유저 dto 만들어야됨