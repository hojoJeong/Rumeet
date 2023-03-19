package com.d204.rumeet.exception;



public enum ErrorEnum {
    SQL_SYNTAX_ERROR("fail","SQL 구문 오류입니다."),
    NO_DATA_ERROR("success","데이터가 없습니다."),
    JWT_ERROR("fail","JWT 만료되었습니다."),
    SQL_ERROR("fail","SQL 오류입니다.");

    public String flag;
    public String msg;

    ErrorEnum(String flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }
}
