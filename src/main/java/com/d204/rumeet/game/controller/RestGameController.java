package com.d204.rumeet.game.controller;

import com.d204.rumeet.game.model.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class RestGameController {

    private final KafkaService kafkaService;


}