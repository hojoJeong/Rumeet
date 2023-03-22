package com.d204.rumeet.user.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserDto {
    int id;
    String email;
    String nickname;
    int age;
    int gender;
    String profileImg;
    Long date;
    int state;


}
