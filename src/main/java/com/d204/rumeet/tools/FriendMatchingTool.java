package com.d204.rumeet.tools;

import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.GameDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class FriendMatchingTool {
    private final RabbitTemplate rabbitTemplate;

    public void doRunning(RaceDto raceDto) {
        // friend.user.{userId}, friend.user.{partnerId} 에 러닝 정보 보내주기
        // 그럼 이제 이 토픽 구독하고 있는 프론트에서 감지하면 달리기 시작!!
        rabbitTemplate.convertAndSend("friend.user.exchange"
                , "friend.user."+raceDto.getUserId(),
                new Gson().toJson(raceDto));
        rabbitTemplate.convertAndSend("friend.user.exchange"
                , "friend.user."+raceDto.getPartnerId(),
                new Gson().toJson(raceDto));
    }
}
