package com.d204.rumeet.game.model.service;

import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.mapper.GameMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        Type type = new TypeToken<Map<String,Integer>>(){}.getType();
        Map<String,Integer> map = new Gson().fromJson(msg,type);
        int mode = gameMapper.getMode(map.get("race_id"));
        int[] km = new int[] {1,2,3,5,1,2,3,5,1,2,3,5};
        kafkaService.sendMessage("rumeet.endgame."+km[mode],msg);
    }

    @Override
    public void endGameToKafka2(Message message) {

    }

    int getRaceId(String msg) {
        String[] tmp = msg.split("race_id\":");
        System.out.println(tmp[1]);
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
