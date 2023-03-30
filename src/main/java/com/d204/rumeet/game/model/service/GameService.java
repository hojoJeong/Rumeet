package com.d204.rumeet.game.model.service;

import com.d204.rumeet.game.model.dto.RaceDto;
import org.springframework.amqp.core.Message;

public interface GameService {
    void makeRace(RaceDto gameDto);

    void endGameToKafka(Message message);

    void endGameToKafka2(Message message);
}
