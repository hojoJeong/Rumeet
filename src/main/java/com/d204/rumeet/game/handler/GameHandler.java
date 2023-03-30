package com.d204.rumeet.game.handler;

import com.d204.rumeet.chat.model.dto.ChatDto;
import com.d204.rumeet.game.model.dto.GameDto;
import com.d204.rumeet.game.model.service.GameService;
import com.d204.rumeet.game.model.service.KafkaService;
import com.d204.rumeet.tools.MatchingTool;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameHandler {

    private final GameService gameService;
    private final MatchingTool matchingTool;

    @RabbitListener(queues = "matching.queue")
    public void matchingHandler(Message message) {
        String msg = new String(message.getBody());
        GameDto info = new Gson().fromJson(msg, GameDto.class);
        matchingTool.doMatching(info);
    }
    @RabbitListener(queues = "cancel.queue")
    public void cancelHandler(Message message) {
        String msg = new String(message.getBody());
        GameDto info = new Gson().fromJson(msg, GameDto.class);
        matchingTool.doCancel(info);
    }

    @RabbitListener(queues = "end.queue")
    public void endHandler(Message message) {
        // TODO
//        gameService.endGameToKafka(message);
    }

    @RabbitListener(queues = "result.queue")
    public void resultHandler(Message message) {
        // TODO
//        gameService.endGameToKafka(message);
    }

}
