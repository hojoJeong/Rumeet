package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import java.io.File

interface UserRepository {
    suspend fun setUserFirstRunCheck(): Boolean
    suspend fun getUserFirstRunCheck(): Boolean
    suspend fun checkDuplicateInfo(type: Int, value: String): NetworkResult<Unit?>
    suspend fun signUpEmail(id: String, password: String, nickname: String, weight: Float, height: Float, gender: Int, age: Int, imageUri: File?): NetworkResult<Unit?>
    suspend fun signUpSocialLogin(oAuth: Long, nickname: String, profileImgUrl: String, weight: Float, height: Float, gender: Int, age: Int, imageUri : File?): NetworkResult<Unit?>
}