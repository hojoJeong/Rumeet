package com.d204.rumeet.user.model.mapper;

import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@Mapper
public interface UserMapper {
    UserDto doLogin(LoginDto loginInfo);
    UserDto getUserById(int id);
}
