package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.sharedpreference.UserDataStorePreferences
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences
) : UserRepository {

    override suspend fun setUserFirstRunCheck() {
        userDataStorePreferences.setFirstRun(false)
    }

    override suspend fun getUserFirstRunCheck(): Boolean {
        return userDataStorePreferences.getFirstRun()
    }
}