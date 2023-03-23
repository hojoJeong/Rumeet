package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult

interface UserRepository {
    suspend fun setUserFirstRunCheck() : Boolean
    suspend fun getUserFirstRunCheck() : Boolean
    suspend fun checkDuplicateInfo(type : Int, value : String) : NetworkResult<Unit?>
}