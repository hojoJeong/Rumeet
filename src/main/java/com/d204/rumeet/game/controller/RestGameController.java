package com.d204.rumeet.game.controller;

import com.d204.rumeet.game.model.dto.GameDto;
import com.d204.rumeet.kafka.model.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class RestGameController {

    private final KafkaService kafkaService;

    @PostMapping("/start")
    public void run(@RequestBody GameDto gameInfo) {
        int gameMode = gameInfo.getGameMode();
        int userId = gameInfo.getUserId();
        String topic = "rumeet.matching." + gameMode;
        System.out.println("topic = " + topic);
        ResponseEntity<?> responseEntity =  kafkaService.messageBYFastApi(gameMode, userId);
        System.out.println(responseEntity.getBody());

        kafkaService.sendMessage(topic, responseEntity.getBody().toString());
    }
}