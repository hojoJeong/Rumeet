package com.d204.rumeet.game.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.MatchAcceptDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.service.GameService;
import com.d204.rumeet.game.model.service.KafkaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class RestGameController {

    private final KafkaService kafkaService;

    private final GameService gameService;

    @Operation(summary = "친구에게 초대받은 러닝 리스트를 가져옴")
    @GetMapping("/invite")
    public ResponseEntity<?> getInvitationList(@PathVariable int userId) {
        RespData<List<FriendRaceDto>> data = new RespData<>();
        data.setData(gameService.getInvitationList(userId));
        return data.builder();
    }

    @Operation(summary = "친구 초대 보내기 (userId, partnerId, mode, date 만 보내면 됩니다)")
    @PostMapping("/invite")
    public ResponseEntity<?> inviteRunning(@RequestBody RaceDto raceDto) {
        RespData<Void> data = new RespData<>();
        gameService.inviteRace(raceDto);
        return data.builder();
    }

    @Operation(summary = "게임 초대에 응답")
    @PostMapping("/invite/accept")
    public ResponseEntity<?> acceptRunning(@PathVariable int raceId) {
        RespData<Void> data = new RespData<>();
        gameService.acceptRace(raceId);
        return data.builder();
    }

    @Operation(summary = "게임 초대에 거부")
    @PostMapping("/invite/deny")
    public ResponseEntity<?> denyRunning(@PathVariable int raceId) {
        RespData<Void> data = new RespData<>();
        gameService.denyRace(raceId);
        return data.builder();
    }

    @Operation(summary = "러닝 방 상태를 가져온다 (0:정상, -1:만료)")
    @GetMapping("/state")
    public ResponseEntity<?> getRunningState(@PathVariable int raceId) {
        RespData<Integer> data = new RespData<>();
        data.setData(gameService.getRaceState(raceId));
        return data.builder();
    }

}