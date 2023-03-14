package com.d204.rumeet.user.model.service;

import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    @Override
    public UserDto doLogin(LoginDto loginDto) {
        return userMapper.doLogin(loginDto);
    }
}
