package com.d204.rumeet.exception;



public enum ErrorEnum {

    // SERVER ERROR
    SQL_SYNTAX_ERROR("fail","SQL 구문 오류입니다.", 1),
    JWT_ERROR("fail","JWT 만료되었습니다.",2),

    // USER ERROR
    NO_USER_ERROR("fail","일치하는 회원이 없습니다." , 2),
    DUPLICATE_USER("fail","중복 되는 이메일/닉네임이 있습니다.",2),

    NO_DATA_ERROR("success","데이터가 없습니다." , 0),
    SQL_ERROR("fail","SQL 오류입니다.",1),
    DUPLICATE_REQUEST_ERROR("fail","이미 친구요청을 보낸 상태입니다.",1),
    NO_REQUEST_ERROR("fail", "해당 친구요청이 존재하지 않습니다." , 1 ),
    NO_FRIEND_ERROR("fail", "친구가 아닙니다.", 1),
    ALREADY_FRIEND_ERROR("fail", "이미 친구입니다.", 1);
    public String flag;
    public String msg;
    public int code;

    ErrorEnum(String flag, String msg, int code) {
        this.flag = flag;
        this.msg = msg;
        this.code = code;
    }
}
