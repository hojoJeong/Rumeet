package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences
) : UserRepository {

    // SP 관련 예외는 후순위
    override suspend fun setUserFirstRunCheck() : Boolean{
        userDataStorePreferences.setFirstRun(false)
        return true
    }

    override suspend fun getUserFirstRunCheck(): Boolean {
        return userDataStorePreferences.getFirstRun()
    }

    override suspend fun getUserAutoLoginCheck(): Boolean {
        return userDataStorePreferences.getAutoLogin()
    }

    override suspend fun setUserAutoLoginCheck() : Boolean{
        userDataStorePreferences.setAutoLogin(true)
        return true
    }
}