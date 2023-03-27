//package com.d204.rumeet.game.controller;
//
//import com.d204.rumeet.game.producer.GameProducer;
//import com.d204.rumeet.kafka.model.KafkaService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//
//@RequestMapping("/game")
//@RequiredArgsConstructor
//@RestController()
//public class RestGameController {
//
//
//    private KafkaService kafkaService;
//
//    @GetMapping("/run")
//    public void run(@RequestParam(value = "roomId") int roomId, @RequestParam(value = "userAId") int userAId, @RequestParam(value = "userBId") int userBId) {
//    /*
//        1. 게임을 시작한다
//        - 방 번호, userAId, userBId, 시작시간
//
//        2. 각 유저에 대해서 토픽을 생성하자
//        - <프로젝트-명>.game-id.<gameID>.user-id.<userID>
//        rummet.game-id.1.user-id.1
//
//        3. 각 유저는 상대 유저의 토픽을 추적하자
//     */
//        // 게임에 참여한 인원 관련 토픽 생성
//        String bootstrapServer = "j8d204.p.ssafy.io:9092";
//        GameProducer userA = new GameProducer(bootstrapServer, roomId, userAId);
//        GameProducer userB = new GameProducer(bootstrapServer, roomId, userBId);
//        String userATopic = userA.createGameTopic();
//        String userBTopic = userB.createGameTopic();
//    }
//
//    @PostMapping("/send/message")
//    @CrossOrigin()
//    public void sendMessage(@RequestParam(value = "topic") String topic, @RequestParam(value = "message") String message) {
//        kafkaService.sendMessage(topic, message);
//    }
//}
