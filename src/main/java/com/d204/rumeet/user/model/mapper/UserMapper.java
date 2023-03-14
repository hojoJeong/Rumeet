package com.d204.rumeet.user.model.mapper;

import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDto doLogin(LoginDto loginInfo);

}
