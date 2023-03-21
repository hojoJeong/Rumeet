package com.d204.rumeet.user.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class KakaoUserDto {
    private String id;
    private String connectedAt;
    private Map<String, String> properties;
    private KakaoAccountDTO kakaoAccount;

}
@Data
class KakaoAccountDTO {
    private boolean profileImageNeedsAgreement;
    private ProfileDTO profile;

}

@Data
class ProfileDTO {
    private String thumbnailImageUrl;
    private String profileImageUrl;
    private boolean isDefaultImage;


}