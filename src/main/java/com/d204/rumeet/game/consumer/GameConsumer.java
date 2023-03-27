//package com.d204.rumeet.game.consumer;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class GameConsumer {
//
//    // 특정 토픽을 할당해야함
//    @KafkaListener(topics = "rummet.game-id.3.user-id.2", groupId = "group-id")
//    public void listenGroupA(String message) {
//        System.out.println("userA: " + message);
//        // 매개변수 message 를 A 에게 전달
//        // ...
//    }
//
//    // 특정 토픽을 할당해야함
//    @KafkaListener(topics = "rummet.game-id.3.user-id.1", groupId = "group-id")
//    public void listenGroupB(String message) {
//        System.out.println("userB: " + message);
//        // 매개변수 message 를 B 에게 전달
//        // ...
//    }
//}
