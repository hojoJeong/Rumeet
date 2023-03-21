package com.d204.rumeet.user.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.exception.CustomJwtException;
import com.d204.rumeet.tools.JwtTool;
import com.d204.rumeet.tools.SHA256;
import com.d204.rumeet.user.model.dto.*;
import com.d204.rumeet.user.model.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserRestController {
    private final UserService userService;
    private final JwtTool jwtTool;

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
        if(token.getRefreshToken() == null
                || token.getRefreshToken().split(" ").length != 2
                || !jwtTool.validateToken(token.getRefreshToken().split(" ")[1])) {
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

    @PostMapping("/join")
    public ResponseEntity<?> joinUser(@RequestPart(value = "user") JoinUserDto user,
                                      @RequestPart(value = "profile_img" , required = false) MultipartFile profile) {
        RespData<Void> data = new RespData<>();
        userService.joinUser(user, profile);
        return data.builder();
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkDuplication(@RequestParam("type") int type, @RequestParam("value") String value) {
        RespData<Void> data = new RespData<>();
        userService.checkDuplication(type, value);
        return data.builder();
    }

    @GetMapping("/email")
    public ResponseEntity<?> emailConfirm(@RequestParam String email) {
        String confirm = userService.sendSimpleMessage(email);
        RespData<String> data = new RespData<>();
        data.setData(confirm);
        return data.builder();
    }

    @PostMapping("/modify/pwd")
    public ResponseEntity<?> modifyPwd(@RequestBody ModifyPwdDto dto) {
        RespData<Void> data = new RespData<>();
        userService.modifyPwd(dto);
        return data.builder();
    }

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoOauth(@RequestParam String code) {
        KakaoUserDto kakaoUser = userService.kakaoOauth(code);
        UserDto user = userService.getUserOauth(kakaoUser.getId());
        if(user == null) {
            RespData<KakaoUserJoinDto> data = new RespData<>();
            data.setFlag("fail");
            data.setCode(1);
            data.setData(new KakaoUserJoinDto(kakaoUser.getId(), kakaoUser.getProperties().get("profile_image")));
            return data.builder();
        }
        RespData<LoginUserDto> data = new RespData<>();
        data.setData(userService.generateUser(user.getId()));
        return data.builder();
    }

    @PostMapping("/oauth/kakao/join")
    public ResponseEntity<?> kakaoOauth(@RequestPart(value = "user") JoinKakaoUserDto user,
                                        @RequestPart(value = "profile_img" , required = false) MultipartFile profile) {
        RespData<Void> data = new RespData<>();
        userService.joinKakaoUser(user, profile);
        return data.builder();
    }

}
