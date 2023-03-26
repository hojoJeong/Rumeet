package com.d204.rumeet.data.remote.dto.response.auth

import com.google.gson.annotations.SerializedName

// 카카오 로그인 시, 회원가입이 필요하면 반환하는 response
internal data class KakaoOAuthResponse(
    @SerializedName("oauth")
    val oauth : Long,
    @SerializedName("profile")
    val profileImg : String?
)