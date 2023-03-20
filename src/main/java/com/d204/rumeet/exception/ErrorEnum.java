package com.d204.rumeet.exception;



public enum ErrorEnum {

    // SERVER ERROR
    SQL_SYNTAX_ERROR("fail","SQL 구문 오류입니다.", 1),
    JWT_ERROR("fail","JWT 만료되었습니다.",1),

    // USER ERROR
    NO_USER_ERROR("fail","일치하는 회원이 없습니다." , 2),
    DUPLICATE_USER_NICKNAME("fail","중복 되는 닉네임이 있습니다.",2),
    DUPLICATE_USER_EMAIL("fail","중복 되는 이메일이 있습니다.",2),

    NO_DATA_ERROR("success","데이터가 없습니다." , 0),
    SQL_ERROR("fail","SQL 오류입니다.",1);

    public String flag;
    public String msg;
    public int code;

    ErrorEnum(String flag, String msg, int code) {
        this.flag = flag;
        this.msg = msg;
        this.code = code;
    }
}
