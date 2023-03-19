package com.d204.rumeet.user.model.service;

import com.d204.rumeet.exception.NoObjectDataException;
import com.d204.rumeet.tools.JwtTool;
import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.LoginUserDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final JwtTool jwtTool;
    private final UserMapper userMapper;
    @Override
    public LoginUserDto doLogin(LoginDto loginDto) {
        UserDto user = userMapper.doLogin(loginDto);
        if(user == null) {
            throw new NoObjectDataException();
        }
        return this.generateUser(user.getId());
    }

    @Override
    public LoginUserDto generateUser(int id) {
        String accessToken = jwtTool.createAccessToken(id);
        String refreshToken = jwtTool.createRefreshToken(id);
        LoginUserDto user = new LoginUserDto(id,accessToken,refreshToken);
        return user;
    }

    @Override
    public UserDto getUserById(int id) {
        UserDto user = userMapper.getUserById(id);
        if(user == null) {
            throw new NoObjectDataException();
        }
        return user;
    }

}
