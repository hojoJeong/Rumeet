package com.d204.rumeet.pipeline.model.service;

import com.d204.rumeet.data.UserEventVO;
import com.google.gson.Gson;
import org.apache.kafka.common.network.Send;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class PipelineServiceImpl implements PipelineService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PipelineServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CompletableFuture<?> sendToTopic(String userAgentName, String colorName, String userName) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
            Date now = new Date();
            Gson gson = new Gson();
            UserEventVO userEventVO = new UserEventVO(sdfDate.format(now),
                    userAgentName, colorName, userName);
            String jsonColorLog = gson.toJson(userEventVO);
            System.out.println(jsonColorLog);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("hello.kafka", jsonColorLog);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("Failed to send Msg" + ex.getMessage());
                } else {
                    System.out.println("Msg sent success");
                }
            });

        return future;
    };



}
