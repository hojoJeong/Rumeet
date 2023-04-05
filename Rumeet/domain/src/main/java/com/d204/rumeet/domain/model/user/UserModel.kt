package com.d204.rumeet.domain.model.user


data class UserModel(
    val id : Int,
    val nickname : String,
    val profileImg : String,
    val totalKm : Double,
    val totalTime : Long,
    val pace: Int
)