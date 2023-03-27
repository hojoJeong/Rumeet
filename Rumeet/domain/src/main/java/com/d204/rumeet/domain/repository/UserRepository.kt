package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import java.io.File

interface UserRepository {
    suspend fun setUserFirstRunCheck(): Boolean
    suspend fun getUserFirstRunCheck(): Boolean

}