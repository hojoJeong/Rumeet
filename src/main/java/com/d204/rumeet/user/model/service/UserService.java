package com.d204.rumeet.user.model.service;

import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.UserDto;

public interface UserService {
    UserDto doLogin(LoginDto loginDto);
}
