package com.d204.rumeet.kafka.model.consumer;

import com.d204.rumeet.game.model.dto.GamePaceDto;
import com.d204.rumeet.tools.MatchingTool;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MatchingConsumer {
    // 일번
    // game stsart 시키는 consumer
    // matching topic 0 ~ 11 구독

    private final MatchingTool matchingTool;
    @KafkaListener(topicPattern = "rumeet\\.game\\.matching\\..*", groupId = "*")
    public void listenMatching(String message) {
        GamePaceDto target = new Gson().fromJson(message, GamePaceDto.class);
        matchingTool.doMatching(target);
    }
}