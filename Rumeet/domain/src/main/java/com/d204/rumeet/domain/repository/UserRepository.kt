package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.UserModel

interface UserRepository {
    suspend fun setUserFirstRunCheck(): Boolean
    suspend fun getUserFirstRunCheck(): Boolean
    suspend fun searchUsers(nickname : String) : NetworkResult<List<UserModel>>
    suspend fun getUserId() : Int
}