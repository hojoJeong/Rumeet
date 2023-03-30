package com.d204.rumeet.badge.controller;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.service.BadgeService;
import com.d204.rumeet.data.RespData;
import com.d204.rumeet.user.model.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/badge")
@RequiredArgsConstructor
@RestController
@Slf4j
public class BadgeController {

    private final BadgeService badgeService;

    @Operation(summary = "뱃지 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBadgeById(@PathVariable int id){
        BadgeDto badge = badgeService.getBadgeById(id);
        RespData<BadgeDto> data = new RespData<>();
        data.setData(badge);
        return data.builder();
    }

    @Operation(summary = "뱃지 추가")
    @PostMapping("/add")
    public ResponseEntity<?> addBadge(@RequestParam("userId") int userId, @RequestParam("badgeId") int badgeId){
        badgeService.addBadge(userId, badgeId);
        RespData<Void> data = new RespData<>();
        data.setMsg("뱃지 추가 완료");
        return data.builder();
    }

    @Operation(summary = "취득한 배지 조회")
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getAllBadgesByUserId(@PathVariable("userId") int userId){
        List<BadgeDto> badges = badgeService.getAllBadgesByUserId(userId);
        RespData<List> data = new RespData<>();
        data.setData(badges);
        return data.builder();
    }
}
