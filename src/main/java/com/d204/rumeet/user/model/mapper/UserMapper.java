package com.d204.rumeet.user.model.mapper;

import com.d204.rumeet.user.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDto> list() throws SQLException;
}
