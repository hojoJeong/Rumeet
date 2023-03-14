package com.d204.rumeet.user.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserRestController {
    private final UserService userService;

    //TODO LIST : 회원가입, JWT처리, 회원 수정, 회원 탈퇴
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto loginDto) {
        RespData<UserDto> data = new RespData<>();
        UserDto userDto = userService.doLogin(loginDto);
        data.setFlag("success");
        data.setMsg("doLogin, null or dto");
        data.setData(userDto);
        return data.builder();
    }

}
