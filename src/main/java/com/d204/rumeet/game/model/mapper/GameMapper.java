package com.d204.rumeet.game.model.mapper;

import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.RaceDto;

import java.util.List;

public interface GameMapper {
    void makeRace(RaceDto gameDto);

    void denyRace(int raceId);

    int getRaceState(int raceId);

    int getMode(int raceId);
}
