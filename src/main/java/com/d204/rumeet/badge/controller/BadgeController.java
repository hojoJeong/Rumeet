package com.d204.rumeet.badge.controller;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.service.BadgeService;
import com.d204.rumeet.data.RespData;
import com.d204.rumeet.user.model.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
