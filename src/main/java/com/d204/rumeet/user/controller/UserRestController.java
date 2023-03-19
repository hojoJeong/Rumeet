package com.d204.rumeet.user.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.exception.CustomJwtException;
import com.d204.rumeet.tools.JwtTool;
import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.LoginUserDto;
import com.d204.rumeet.user.model.dto.UserDto;
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

    //TODO LIST : 회원가입, 회원 수정, 회원 탈퇴

    // 유저 정보 불러오기
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        RespData<UserDto> data = new RespData<>();
        System.out.println(id);
        UserDto user = userService.getUserById(id);
        data.setFlag("success");
        data.setMsg("");
        data.setData(user);
        return data.builder();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto loginDto) {
        RespData<LoginUserDto> data = new RespData<>();
        LoginUserDto user = userService.doLogin(loginDto);
        data.setFlag("success");
        data.setMsg("");
        data.setData(user);
        return data.builder();
    }

    // 리프레시 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> reToken(@RequestBody HashMap<String,String> token) {
        RespData<LoginUserDto> data = new RespData<>();
        String refreshToken = token.get("refreshToken");
        int id = Integer.parseInt(token.get("id"));

        if(!jwtTool.validateToken(refreshToken)) {
            throw new CustomJwtException();
        }

        LoginUserDto user = userService.generateUser(id);
        data.setFlag("success");
        data.setMsg("");
        data.setData(user);
        return data.builder();
    }



}
