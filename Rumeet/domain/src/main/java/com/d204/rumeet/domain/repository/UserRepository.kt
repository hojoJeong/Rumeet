package com.d204.rumeet.domain.repository

interface UserRepository {
    suspend fun setUserFirstRunCheck() : Boolean
    suspend fun getUserFirstRunCheck() : Boolean
}