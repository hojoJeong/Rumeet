package com.d204.rumeet.game.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.game.model.dto.FriendRaceDto;
import com.d204.rumeet.game.model.dto.MatchAcceptDto;
import com.d204.rumeet.game.model.dto.RaceDto;
import com.d204.rumeet.game.model.dto.SoloPlayDto;
import com.d204.rumeet.game.model.service.GameService;
import com.d204.rumeet.game.model.service.KafkaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.util.GSetByHashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class RestGameController {

    private final KafkaService kafkaService;

    private final GameService gameService;

    @Operation(summary = "친구에게 초대받은 러닝 리스트를 가져옴")
    @GetMapping("/invite/{userId}")
    public ResponseEntity<?> getInvitationList(@PathVariable int userId) {
        RespData<List> data = new RespData<>();
        data.setMsg("초대받은 리스트");
        data.setData(gameService.getInvitationList(userId));
        return data.builder();
    }

    @Operation(summary = "친구에게 러닝 초대 보내고 raceId를 리턴 (userId, partnerId, mode, date(30초 타이머 시작한 시간 기준으로 long type) 만 보내면 됩니다)")
    @PostMapping("/invite")
    public ResponseEntity<?> inviteRunning(@RequestBody RaceDto raceDto) {
        RespData<Integer> data = new RespData<>();
        data.setMsg("친구 초대");
        data.setData(gameService.inviteRace(raceDto));
        return data.builder();
    }

    @Operation(summary = "게임 초대에 응답")
    @PostMapping("/{raceId}/accept")
    public ResponseEntity<?> acceptRunning(@PathVariable int raceId) {
        RespData<Void> data = new RespData<>();
        data.setMsg("초대 수락");
        gameService.acceptRace(raceId);
        return data.builder();
    }

    @Operation(summary = "게임 초대에 거부, or 30초 뒤 타임아웃 후 state -1로 변경")
    @PostMapping("/{raceId}/deny")
    public ResponseEntity<?> denyRunning(@PathVariable int raceId) {
        RespData<Void> data = new RespData<>();
        data.setMsg("초대 거부");
        gameService.denyRace(raceId);
        return data.builder();
    }

    @Operation(summary = "러닝 방 상태를 가져온다 (0:정상, -1:만료)")
    @GetMapping("/{raceId}/state")
    public ResponseEntity<?> getRunningState(@PathVariable int raceId) {
        RespData<Integer> data = new RespData<>();
        data.setMsg("러닝 방 상태");
        data.setData(gameService.getRaceState(raceId));
        return data.builder();
    }

    @Operation(summary = "솔로플레이 시작 (parameter userId, mode, ghost)")
    @GetMapping("/solo")
    public ResponseEntity<?> doSoloPlay(@RequestParam int userId, @RequestParam int mode,@RequestParam int ghost) {
        RespData<SoloPlayDto> data = new RespData<>();
        data.setMsg("솔플 시작");
        data.setData(gameService.doSoloPlay(userId,mode,ghost));
        return data.builder();
    }


    @Operation(summary = "메인페이지 추천 3인 (Path userId)")
    @GetMapping("/recommend/{userId}")
    public ResponseEntity<?> recommendMainPage(@PathVariable int userId) {
        RespData<List> data = new RespData<>();
        data.setMsg("메인페이지 추천 3인");
        data.setData(gameService.recommendMainPage(userId));
        return data.builder();
    }

    @PostMapping("/poly")
    public ResponseEntity<?> savePoly(@RequestPart("polyline") MultipartFile file) {
        RespData<String> data = new RespData<>();
        data.setData(gameService.savePoly(file));
        return data.builder();
    }
}