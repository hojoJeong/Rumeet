package com.d204.rumeet.user.model.service;

import com.d204.rumeet.user.model.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    LoginUserDto doLogin(LoginDto loginDto);

    LoginUserDto generateUser(int id);

    UserDto getUserById(int id);

    void modifyUser(ModifyUserDto user);

    void delUser(int id);

    void joinUser(JoinUserDto user, MultipartFile profile);

    void checkDuplication(int type, String value);

    String sendSimpleMessage(String email);

    List<SimpleUserDto> searchUsersByNickname(String nickname);

}
