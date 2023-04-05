package com.d204.rumeet.game.model.mapper;

import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.dto.RaceStateDto;

import java.util.List;

public interface GameMapper {
    void makeRace(RaceDto gameDto);

    int denyRace(int raceId);

    RaceStateDto getRaceState(int raceId);

    int getMode(int raceId);

    RaceDto getRace(int raceId);
}
