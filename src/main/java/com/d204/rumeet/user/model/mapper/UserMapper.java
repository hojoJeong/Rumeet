package com.d204.rumeet.user.model.mapper;

import com.d204.rumeet.user.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

@Mapper
public interface UserMapper {
    UserDto doLogin(LoginDto loginInfo);
    UserDto getUserById(int id);

    int modifyUser(ModifyUserDto user);

    int delUser(int id);

    void joinUser(JoinUserDto user);

    int checkDuplication(CheckDto checkDto);

    List<SimpleUserDto> searchUsersByNickname(String nickname);
}
