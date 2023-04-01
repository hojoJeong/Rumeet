package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.UserApi
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.user.FcmTokenRequestDto
import com.d204.rumeet.data.remote.dto.request.user.ModifyNickNameRequest
import com.d204.rumeet.data.remote.dto.request.user.ModifyNotificationStateRequestDto
import com.d204.rumeet.data.remote.dto.response.user.*
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.data.remote.mapper.toRequestDto
import com.d204.rumeet.data.util.getMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.*
import com.d204.rumeet.domain.onSuccess
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

    override suspend fun modifyProfileImgAndNickName(profile: ModifyProfileAndNickNameDomainModel): Boolean {
        val user = ModifyNickNameRequest(profile.id, profile.name, profile.curProfile)
        val file = getMultipartData(profile.profile)
        Log.d(TAG, "modifyProfileImgAndNickName: $user , $file")
        return userApi.modifyProfileAndNickName(user, file).flag == "success"
    }

    override suspend fun registFcmToken(userId: Int, token: String): Boolean {
        val request = FcmTokenRequestDto(userId, token)
        return userApi.registFcmToken(request).flag == "success"
    }

    override suspend fun modifyNotificationSettingState(
        userId: Int,
        target: Int,
        state: Int
    ): Boolean {
        val request = ModifyNotificationStateRequestDto(userId, target, state)
        return userApi.modifyNotificationSettingState(request).flag == "success"
    }

    override suspend fun getNotificationSettingState(userId: Int): NetworkResult<NotificationStateDomainModel> {
        return handleApi { userApi.getNotificationSettingState(userId) }.toDomainResult<NotificationSettingStateResponseDto, NotificationStateDomainModel> { it.toDomainModel() }
    }

    override suspend fun getRunningRecord(
        userId: Int,
        startDate: Long,
        endDate: Long
    ): NetworkResult<RunningRecordDomainModel> {
        return handleApi {
            userApi.getRunningRecord(
                userId,
                startDate,
                endDate
            )
        }.toDomainResult<RunningRecordResponseDto, RunningRecordDomainModel> { it.toDomainModel() }
    }

    override suspend fun getHomeData(userId: Int): NetworkResult<HomeDataDomainModel> {
        val response =
            handleApi { userApi.getHomeData(userId) }.toDomainResult<HomeDataResponseDto, HomeDataDomainModel> { it.toDomainModel() }
        return response
    }

    override suspend fun getFriendRequestList(): NetworkResult<List<NotificationListDomainModel>> {
        return handleApi { userApi.getFriendRequestList() }.toDomainResult<List<NotificationListResponseDto>, List<NotificationListDomainModel>> { it.map { model -> model.toDomainModel() } }
    }
}
