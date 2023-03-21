package com.d204.rumeet.user.model.dto;

import lombok.Data;

@Data
public class NaverUserDto {
    String resultcode;
    String message;
    NaverUserInfoDto response;

}

