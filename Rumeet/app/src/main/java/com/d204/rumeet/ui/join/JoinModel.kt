package com.d204.rumeet.ui.join

import okhttp3.MultipartBody

data class JoinModel(
    var id: String = "",
    var socialJoinModel: SocialJoinModel? = null,
    var profileImg: MultipartBody.Part? = null,
    var nickname: String = "",
    var password: String = ""
)

data class SocialJoinModel(
    val oauth: Long,
    val profileImgUrl : String
)