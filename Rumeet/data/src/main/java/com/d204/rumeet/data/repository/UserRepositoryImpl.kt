package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences
) : UserRepository {

    // SP 관련 예외는 후순위
    override suspend fun setUserFirstRunCheck(): Boolean {
        try {
            userDataStorePreferences.setFirstRun(true)
        } catch (e: IOException) {
            return false
        }
        return true
    }

    override suspend fun getUserFirstRunCheck(): Boolean {
        return userDataStorePreferences.getFirstRun()
    }

    override suspend fun getUserAutoLoginCheck(): Boolean {
        return userDataStorePreferences.getAutoLogin()
    }

    override suspend fun setUserAutoLoginCheck(state: Boolean): Boolean {
        try {
            userDataStorePreferences.setAutoLogin(state)
        } catch (e: IOException) {
            return false
        }
        return true
    }
}