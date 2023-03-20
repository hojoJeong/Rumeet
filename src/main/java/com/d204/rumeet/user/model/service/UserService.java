package com.d204.rumeet.user.model.service;

import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.LoginUserDto;
import com.d204.rumeet.user.model.dto.ModifyUserDto;
import com.d204.rumeet.user.model.dto.UserDto;

public interface UserService {
    LoginUserDto doLogin(LoginDto loginDto);

    LoginUserDto generateUser(int id);

    UserDto getUserById(int id);

    void modifyUser(ModifyUserDto user);

    void delUser(int id);
}
