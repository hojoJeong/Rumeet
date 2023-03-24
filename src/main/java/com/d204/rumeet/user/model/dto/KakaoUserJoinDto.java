package com.d204.rumeet.user.model.dto;

import lombok.Data;

@Data
public class KakaoUserJoinDto {
    int id;
    String oauth;
    String profile;

    public KakaoUserJoinDto(String oauth, String profile) {
        this.oauth = oauth;
        this.profile = profile;
    }
}
