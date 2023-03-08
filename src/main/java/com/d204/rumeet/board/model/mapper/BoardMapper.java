package com.d204.rumeet.board.model.mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface BoardMapper {
    List<Object> listBoard(HashMap<String, Object> param) throws SQLException;
}
