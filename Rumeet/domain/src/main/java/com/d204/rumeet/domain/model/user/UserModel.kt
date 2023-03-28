package com.d204.rumeet.domain.model.user


data class UserModel(
    val id : Int,
    val email : String,
    val nickname : String,
    val age : Int,
    val gender : Int,
    val profileImg : String,
    val createdAt : Long,
    val state : Int
)