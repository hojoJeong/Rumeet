package com.d204.rumeet.game.model.service;

import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.MatchAcceptDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import org.springframework.amqp.core.Message;

import java.util.List;

public interface GameService {
    void makeRace(RaceDto gameDto);

    void inviteRace(RaceDto raceDto);

    int getRaceState(int raceId);

    void acceptRace(int raceId);

    void denyRace(int raceId);

    List<FriendRaceDto> getInvitationList(int userId);

    void endGameToKafka(Message message) throws Exception;

}
