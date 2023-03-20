package com.d204.rumeet.user.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.exception.CustomJwtException;
import com.d204.rumeet.tools.JwtTool;
import com.d204.rumeet.user.model.dto.*;
import com.d204.rumeet.user.model.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserRestController {
    private final UserService userService;

    private final JwtTool jwtTool;

    //TODO LIST : 회원가입

    // 유저 정보 불러오기
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        UserDto user = userService.getUserById(id);
        RespData<UserDto> data = new RespData<>();
        data.setData(user);
        return data.builder();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto loginDto) {
        LoginUserDto user = userService.doLogin(loginDto);
        RespData<LoginUserDto> data = new RespData<>();
        data.setData(user);
        return data.builder();
    }

    // 리프레시 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> reToken(@RequestBody ReTokenDto token) {
        if(!jwtTool.validateToken(token.getRefreshToken())) {
            throw new CustomJwtException();
        }

        LoginUserDto user = userService.generateUser(token.getId());
        RespData<LoginUserDto> data = new RespData<>();
        data.setData(user);
        return data.builder();
    }
    
    // 유저 업데이트
    @PutMapping
    public ResponseEntity<?> modifyUser(@RequestBody ModifyUserDto user) {
        userService.modifyUser(user);
        RespData<Void> data = new RespData<>();
        data.setData(null);
        return data.builder();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delUser(@PathVariable int id) {
        userService.delUser(id);
        RespData<Void> data = new RespData<>();
        data.setData(null);
        return data.builder();
    }

//    @PostMapping("/join")
//    public ResponseEntity<?> joinUser(@RequestBody JoinUserDto) {
//
//    }

}
