package com.d204.rumeet.exception;

import com.d204.rumeet.data.RespData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> SqlException(SQLException ex) {
        RespData<Void> data = new RespData(ErrorEnum.SQL_ERROR);
        if(ex instanceof SQLSyntaxErrorException) {
            data = new RespData(ErrorEnum.SQL_SYNTAX_ERROR);
        }
        return data.builder();
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<?> JwtException() {
        RespData<Void> data = new RespData(ErrorEnum.JWT_ERROR);
        return data.builder();
    }

    @ExceptionHandler(NoUserDataException.class)
    public ResponseEntity<?> NoObjectDataException() {
        RespData<Void> data = new RespData(ErrorEnum.NO_USER_ERROR);
        data.setData(null);
        return data.builder();
    }

    @ExceptionHandler(NoListDataException.class)
    public ResponseEntity<?> NoListDataException() {
        RespData<ArrayList> data = new RespData(ErrorEnum.NO_DATA_ERROR);
        data.setData(new ArrayList<>());
        return data.builder();
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> DuplicateException() {
        RespData<Void> data = new RespData(ErrorEnum.DUPLICATE_USER);
        data.setData(null);
        return data.builder();
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<?> DuplicateRequestException() {
        RespData<Void> data = new RespData(ErrorEnum.DUPLICATE_REQUEST_ERROR);
        data.setData(null);
        return data.builder();
    }

    @ExceptionHandler(NoRequestException.class)
    public ResponseEntity<?> NoRequestException() {
        RespData<Void> data = new RespData(ErrorEnum.NO_REQUEST_ERROR);
        data.setData(null);
        return data.builder();
    }

    @ExceptionHandler(NoFriendDataException.class)
    public ResponseEntity<?> NoFriendDataException() {
        RespData<Void> data = new RespData(ErrorEnum.NO_FRIEND_ERROR);
        data.setData(null);
        return data.builder();
    }
}
