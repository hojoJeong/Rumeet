package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.SignApiService
import com.d204.rumeet.data.remote.api.UserApi
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.user.JoinRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.data.util.getMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import com.d204.rumeet.domain.repository.UserRepository
import com.d204.rumeet.domain.toDomainResult
import java.io.File
import java.io.IOException
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences,
    private val userApi: UserApi
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

    override suspend fun getUserId(): Int {
        return userDataStorePreferences.getUserId()
    }

    override suspend fun getUserInfo(userId: Int): NetworkResult<UserInfoDomainModel> {
        return handleApi { userApi.getUserInfo(userId) }.toDomainResult<UserInfoResponse, UserInfoDomainModel> { it.toDomainModel() }
    }


}