package com.d204.rumeet.user.model.mapper;

import com.d204.rumeet.user.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@Mapper
public interface UserMapper {
    UserDto doLogin(LoginDto loginInfo);

    UserDto getUserById(int id);

    int modifyUser(ModifyUserDto user);

    int delUser(int id);

    void joinUser(JoinUserDto user);

    int checkDuplication(CheckDto checkDto);

    int modifyPwd(ModifyPwdDto modifyPwdDto);

    UserDto getUserOauth(String tokenId);

    void joinKakaoUser(JoinKakaoUserDto user);

    int modifyUserProfile(ProfileUserDto user);
}
