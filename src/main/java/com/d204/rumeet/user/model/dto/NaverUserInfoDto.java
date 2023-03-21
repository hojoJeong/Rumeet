package com.d204.rumeet.user.model.dto;

import lombok.Data;

@Data
public class NaverUserInfoDto {
    String id;
    String nickname;
    String profile_image;
    String age;
    String gender;
    String email;
    String birthday;
    String birthyear;
}