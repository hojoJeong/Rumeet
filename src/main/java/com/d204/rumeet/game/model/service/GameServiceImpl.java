package com.d204.rumeet.game.model.service;

import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.mapper.GameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameMapper gameMapper;
    public void makeRace(RaceDto raceDto) {

        gameMapper.makeRace(raceDto);
    }
}
