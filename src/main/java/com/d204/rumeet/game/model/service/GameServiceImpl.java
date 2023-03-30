package com.d204.rumeet.game.model.service;

import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.mapper.GameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameMapper gameMapper;
    private final KafkaService kafkaService;
    public void makeRace(RaceDto raceDto) {

        gameMapper.makeRace(raceDto);
    }

    @Override
    public void endGameToKafka(Message message) {
        String msg = new String(message.getBody());
        int mode = getMode(msg);
        int[] km = new int[] {1,2,3,5,1,2,3,5,1,2,3,5};
        kafkaService.sendMessage("rumeet.endgame."+km[mode],msg);
    }

    @Override
    public void endGameToKafka2(Message message) {

    }

    int getMode(String msg) {
        String[] tmp = msg.split("mode\":");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< tmp[1].length(); i++) {
            char c = tmp[1].charAt(i);
            if( c>= '0' && c<='9') {
                sb.append(c);
            } else {
                break;
            }
        }
        return Integer.parseInt(sb.toString());
    }
}
