package com.d204.rumeet.board.model.service;

import com.d204.rumeet.board.model.mapper.BoardMapper;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class BoarderviceImpl implements BoardService {

    BoardMapper mapper;
    @Override
    public List<Object> listBoard(HashMap<String, Object> map) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        List<Object> boardList = null;

        try {
            boardList = mapper.listBoard(param);
            if (boardList == null && boardList.isEmpty()) {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return boardList;
    }
}
