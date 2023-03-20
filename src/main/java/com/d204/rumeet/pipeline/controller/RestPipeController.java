package com.d204.rumeet.pipeline.controller;

import com.d204.rumeet.data.UserEventVO;
import com.d204.rumeet.pipeline.model.service.PipelineService;
import com.d204.rumeet.pipeline.model.service.PipelineServiceImpl;
import com.google.gson.Gson;
import org.apache.kafka.common.network.Send;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


@RestController
public class RestPipeController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private PipelineService service;

    public RestPipeController(KafkaTemplate<String, String> kafkaTemplate, PipelineService service) {
        this.kafkaTemplate = kafkaTemplate;
        this.service = service;
    }


    @GetMapping("/api/select")
    public void selectColor(
            @RequestHeader("user-agent")String userAgentName,
            @RequestParam(value = "color")String colorName,
            @RequestParam(value = "user")String userName) {

        CompletableFuture<?> future = service.sendToTopic(userAgentName, colorName, userName);

        System.out.println(future);
    }
}
