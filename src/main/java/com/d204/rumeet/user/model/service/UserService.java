package com.d204.rumeet.user.model.service;

import com.d204.rumeet.user.model.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    LoginUserDto doLogin(LoginDto loginDto);

    LoginUserDto generateUser(int id);

    UserDto getUserById(int id);

    void modifyUser(ModifyUserDto user);

    void delUser(int id);

    void joinUser(JoinUserDto user, MultipartFile profile);

    void checkDuplication(int type, String value);

    String sendSimpleMessage(String email);

    void modifyPwd(ModifyPwdDto dto);

    KakaoUserDto kakaoOauth(String code);

    UserDto getUserOauth(String tokenId);

    void joinKakaoUser(JoinKakaoUserDto user, MultipartFile profile);
}
