package com.d204.rumeet.domain.repository

interface UserRepository {
    suspend fun setUserFirstRunCheck()
    suspend fun getUserFirstRunCheck() : Boolean
}