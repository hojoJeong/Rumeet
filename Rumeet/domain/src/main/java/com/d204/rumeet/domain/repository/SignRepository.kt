package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import java.io.File

interface SignRepository {
    suspend fun checkDuplicateInfo(type: Int, value: String): NetworkResult<Unit?>
    suspend fun signUpEmail(id: String, password: String, nickname: String, weight: Float, height: Float, gender: Int, age: Int, imageUri: File?): NetworkResult<Unit?>
    suspend fun signUpSocialLogin(oAuth: Long, nickname: String, profileImgUrl: String, weight: Float, height: Float, gender: Int, age: Int, imageUri : File?): NetworkResult<Unit?>
    suspend fun requestAuthenticationCode(email : String) : NetworkResult<String?>
}