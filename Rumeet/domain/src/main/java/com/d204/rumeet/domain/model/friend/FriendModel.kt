package com.d204.rumeet.domain.model.friend

data class FriendModel(
    val userId : Int,
    val email : String,
    val nickname : String,
    val age : Int,
    val gender : Int,
    val profileImg : String,
    val beFriendAt : Long,
    val state : Int
)