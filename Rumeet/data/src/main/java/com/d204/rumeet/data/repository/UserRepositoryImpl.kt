package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.UserApi
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.response.user.AcquiredBadgeResponse
import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.data.remote.mapper.toRequestDto
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.AcquiredBadgeListDomainModel
import com.d204.rumeet.domain.model.user.ModifyUserDetailInfoDomainModel
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import com.d204.rumeet.domain.repository.UserRepository
import com.d204.rumeet.domain.toDomainResult
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
        val response =
            handleApi { userApi.getUserInfo(userId) }.toDomainResult<UserInfoResponse, UserInfoDomainModel> { it.toDomainModel() }
        Log.d(TAG, "getUserInfo: $response")
        return response
    }

    override suspend fun modifyUserDetailInfo(userInfo: ModifyUserDetailInfoDomainModel): Boolean {
        return userApi.modifyUserDetailInfo(userInfo.toRequestDto()).flag == "success"
    }

    override suspend fun withdrawal(userId: Int): Boolean {
        return userApi.withdrawal(userId).flag == "success"
    }

    override suspend fun getAcquiredBadgeList(userId: Int): NetworkResult<List<AcquiredBadgeListDomainModel>> {
        return handleApi { userApi.getAcquiredBadgeList(userId) }.toDomainResult<List<AcquiredBadgeResponse>, List<AcquiredBadgeListDomainModel>> { it.map { model -> model.toDomainModel() } }
    }

    override suspend fun logout() {
        userDataStorePreferences.clearUserInfo()
    }
}