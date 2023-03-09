package com.d204.rumeet.user.model.service;

import com.d204.rumeet.data.HttpVO;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserMapper userMapper;
    HttpVO http = null;
    @Override
    public HttpVO list() throws Exception{
        http = new HttpVO();
        http.setFlag("success");
        List<UserDto> data = userMapper.list();
        http.setData(data);
        return http;
    }
}
