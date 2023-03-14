package com.d204.rumeet;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.user.model.dto.LoginDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RumeetApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void doLoginTest(){
        Assertions.assertThat(userMapper).isNotNull();
        LoginDto loginDto = new LoginDto("test","test");
        UserDto userDto = userMapper.doLogin(loginDto);
        Assertions.assertThat(userDto.getNickname()).isEqualTo("test");
        RespData<UserDto> testx = new RespData<>();
        testx.setData(userDto);
        testx.setFlag("success");
        testx.setMsg("ok");
        Assertions.assertThat(testx.builder()).isNotNull();
        System.out.println("testx.builder() = " + testx.builder());
    }
}
