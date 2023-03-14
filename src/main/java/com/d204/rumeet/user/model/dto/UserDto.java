package com.d204.rumeet.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    int id;
    String email;
    String password;
    String nickname;
    int age;
    int gender;
    String profileImg;
    float height;
    float weight;
    String Oauth;
    String refreshToken;
    Long date;
    int state;

}
