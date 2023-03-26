package com.d204.rumeet.user.model.dto;

import lombok.Data;

@Data
public class ModifyPwdDto {
    int id;
    String email;
    String password;
}
