package com.d204.rumeet.user.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.exception.CustomJwtException;
import com.d204.rumeet.exception.ErrorEnum;
import com.d204.rumeet.tools.DataUtil;
import com.d204.rumeet.tools.JwtTool;
import com.d204.rumeet.user.model.dto.*;
import com.d204.rumeet.user.model.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserRestController {
    private final UserService userService;
    private final JwtTool jwtTool;

    private final DataUtil dataUtil;
    // 유저 정보 불러오기
    @Operation(summary = "유저 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        UserDto user = userService.getUserById(id);
        RespData<UserDto> data = new RespData<>();
        data.setData(user);
        return data.builder();
    }

    // 로그인
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto loginDto) {
        LoginUserDto user = userService.doLogin(loginDto);
        RespData<LoginUserDto> data = new RespData<>();
        data.setData(user);
        return data.builder();
    }

    // 리프레시 토큰 재발급
    @Operation(summary = "리프레시 토큰 재발급")
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
    @Operation(summary = "유저 정보 업데이트", description = "성별, 나이, 키, 몸무게 업데이트")
    @PutMapping
    public ResponseEntity<?> modifyUser(@RequestBody ModifyUserDto user) {
        userService.modifyUser(user);
        RespData<Void> data = new RespData<>();
        data.setData(null);
        return data.builder();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delUser(@PathVariable int id) {
        userService.delUser(id);
        RespData<Void> data = new RespData<>();
        data.setData(null);
        return data.builder();
    }
    @Operation(summary = "프로필 사진 변경")
    @PostMapping("/modify/profile")
    public ResponseEntity<?> modifyUserProfile(@RequestPart(value = "user") ProfileUserDto user,
                                      @RequestPart(value = "profile_img") MultipartFile profile) {
        RespData<Void> data = new RespData<>();
        userService.modifyUserProfile(user, profile);
        return data.builder();
    }
    @GetMapping("/hdfs")
    public ResponseEntity<?> hdfs() {
        RespData<Void> data = new RespData<>();
        dataUtil.load();
        return data.builder();
    }

    @Operation(summary = "회원 가입")
    @PostMapping("/join")
    public ResponseEntity<?> joinUser(@RequestPart(value = "user") JoinUserDto user,
                                      @RequestPart(value = "profile_img" , required = false) MultipartFile profile) {
        RespData<Void> data = new RespData<>();
        System.out.println(profile);
        userService.joinUser(user, profile);
        System.out.println(profile);

        return data.builder();
    }

    @Operation(summary = "닉네임, 이메일 중복검사", description = "type - 1 : nickname, 2 : email / value - 중복 검사할 데이터")
    @GetMapping("/check")
    public ResponseEntity<?> checkDuplication(@RequestParam("type") int type, @RequestParam("value") String value) {
        RespData<Void> data = new RespData<>();
        userService.checkDuplication(type, value);
        return data.builder();
    }

    @Operation(summary = "이메일 인증 코드 전송", description = "Email 인증 코드를 전송한다.\n" +
            "이메일로 올바른 코드가 전송되고,\n" +
            "Data는 SHA256로 해싱된 정보가 넘어간다.\n" +
            "Data를 저장하고 유저가 입력한 코드를 SHA256으로 인코딩하여 일치 여부를 확인하면된다.")
    @GetMapping("/email")
    public ResponseEntity<?> emailConfirm(@RequestParam String email) {
        String confirm = userService.sendSimpleMessage(email);
        RespData<String> data = new RespData<>();
        data.setData(confirm);
        return data.builder();
    }

    // 닉네임으로 유저 검색
    @Operation(summary = "유저 검색 (닉네임)", description = "전체 사용자에서 닉네임 검색")
    @GetMapping("/search")
    public ResponseEntity<?> searchFriend(@RequestParam("nickname") String nickname) {
        List<SimpleUserDto> users = userService.searchUsersByNickname(nickname);
        RespData<List> data = new RespData<>();
        data.setData(users);
        return data.builder();
    }

    @Operation(summary = "비밀번호 변경")
    @PostMapping("/modify/pwd")
    public ResponseEntity<?> modifyPwd(@RequestBody ModifyPwdDto dto) {
        RespData<Void> data = new RespData<>();
        userService.modifyPwd(dto);
        return data.builder();
    }

    @Operation(summary = "카카오 로그인")
    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoOauth(@RequestParam String code) {
        KakaoUserDto kakaoUser = userService.kakaoOauth(code);
        UserDto user = userService.getUserOauth(kakaoUser.getId());
        if(user == null) {
            RespData<KakaoUserJoinDto> data = new RespData<>();
            data.setFlag("fail");
            data.setCode(2);
            data.setMsg("회원가입");
            data.setData(new KakaoUserJoinDto(kakaoUser.getId(), kakaoUser.getProperties().get("profile_image")));
            return data.builder();
        }
        if(user.getState() == -1) {
            RespData<LoginUserDto> data = new RespData<>();
            data.setFlag("fail");
            data.setCode(ErrorEnum.DELETED_USER.code);
            data.setMsg("이미 탈퇴한 회원입니다.");
            data.setData(null);
            return data.builder();
        }
        RespData<LoginUserDto> data = new RespData<>();
        data.setData(userService.generateUser(user.getId()));
        return data.builder();
    }

    @Operation(summary = "네이버 로그인")
    @GetMapping("/oauth/naver")
    public ResponseEntity<?> naverOauth(@RequestParam String code) {
        NaverUserDto naverUserDto = userService.naverOauth(code);
        UserDto user = userService.getUserOauth(naverUserDto.getResponse().getId());
        if(user == null) {
            RespData<KakaoUserJoinDto> data = new RespData<>();
            data.setFlag("fail");
            data.setCode(2);
            data.setMsg("회원가입");
            data.setData(new KakaoUserJoinDto(naverUserDto.getResponse().getId(), naverUserDto.getResponse().getProfile_image()));
            return data.builder();
        }
        if(user.getState() == -1) {
            RespData<LoginUserDto> data = new RespData<>();
            data.setFlag("fail");
            data.setCode(ErrorEnum.DELETED_USER.code);
            data.setMsg("이미 탈퇴한 회원입니다.");
            data.setData(null);
            return data.builder();
        }
        RespData<LoginUserDto> data = new RespData<>();
        data.setData(userService.generateUser(user.getId()));
        return data.builder();
    }

    @Operation(summary = "카카오/네이버로 회원가입")
    @PostMapping("/oauth/join")
    public ResponseEntity<?> kakaoOauth(@RequestPart(value = "user") JoinKakaoUserDto user,
                                        @RequestPart(value = "profile_img" , required = false) MultipartFile profile) {
        RespData<Void> data = new RespData<>();
        userService.joinKakaoUser(user, profile);
        return data.builder();
    }

    @Operation(summary = "이메일을 통한 유저 가입 유무 조회")
    @GetMapping("check/email")
    public ResponseEntity<?> checkExistsUser(@RequestParam String email) {
        int userId = userService.checkExistsUser(email);
        RespData<Integer> data = new RespData<>();
        if (userId == 0) {
            data.setFlag("fail");
            data.setCode(2);
            data.setMsg("회원가입");
            data.setData(0);
        }
        data.setData(userId);
        return data.builder();
    }
}
