package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.UserApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import com.d204.rumeet.data.remote.dto.response.user.UserResponseDto
import com.d204.rumeet.data.remote.dto.response.user.toDomainModel
import com.d204.rumeet.data.remote.dto.request.user.FcmTokenRequestDto
import com.d204.rumeet.data.remote.dto.request.user.ModifyNickNameRequest
import com.d204.rumeet.data.remote.dto.request.user.ModifyNotificationStateRequestDto
import com.d204.rumeet.data.remote.dto.response.user.*
import com.d204.rumeet.data.remote.mapper.toDomain
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.data.remote.mapper.toRequestDto
import com.d204.rumeet.data.util.getProfileMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendRecommendDomainModel
import com.d204.rumeet.domain.model.user.*
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import com.d204.rumeet.domain.model.user.UserModel
import com.d204.rumeet.domain.repository.UserRepository
import com.d204.rumeet.domain.toDomainResult
import java.io.IOException
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences,
    private val userApiService: UserApiService,
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
        Log.d(TAG, "getUserId repo: ${userDataStorePreferences.getUserId()}")
        return userDataStorePreferences.getUserId()
    }

    override suspend fun getUserInfo(userId: Int): NetworkResult<UserInfoDomainModel> {
        val response =
            handleApi { userApiService.getUserInfo(userId) }.toDomainResult<UserInfoResponse, UserInfoDomainModel> { it.toDomainModel() }
        Log.d(TAG, "getUserInfo: $response")
        return response
    }

    override suspend fun modifyUserDetailInfo(userInfo: ModifyUserDetailInfoDomainModel): Boolean {
        return userApiService.modifyUserDetailInfo(userInfo.toRequestDto()).flag == "success"
    }

    override suspend fun withdrawal(userId: Int): Boolean {
        return userApiService.withdrawal(userId).flag == "success"
    }

    override suspend fun getAcquiredBadgeList(userId: Int): NetworkResult<List<AcquiredBadgeListDomainModel>> {
        return handleApi { userApiService.getAcquiredBadgeList(userId) }.toDomainResult<List<AcquiredBadgeResponse>, List<AcquiredBadgeListDomainModel>> { it.map { model -> model.toDomainModel() } }
    }

    override suspend fun logout() {
        userDataStorePreferences.clearUserInfo()
    }

    override suspend fun modifyProfileImgAndNickName(profile: ModifyProfileAndNickNameDomainModel): Boolean {
        val user = ModifyNickNameRequest(profile.id, profile.name, profile.curProfile)
        val file = getProfileMultipartData(profile.profile)
        Log.d(TAG, "modifyProfileImgAndNickName: $user , $file")
        return userApiService.modifyProfileAndNickName(user, file).flag == "success"
    }

    override suspend fun registFcmToken(userId: Int, token: String): Boolean {
        val request = FcmTokenRequestDto(userId, token)
        return userApiService.registFcmToken(request).flag == "success"
    }

    override suspend fun modifyNotificationSettingState(
        userId: Int,
        target: Int,
        state: Int
    ): Boolean {
        val request = ModifyNotificationStateRequestDto(userId, target, state)
        return userApiService.modifyNotificationSettingState(request).flag == "success"
    }

    override suspend fun getNotificationSettingState(userId: Int): NetworkResult<NotificationStateDomainModel> {
        return handleApi { userApiService.getNotificationSettingState(userId) }.toDomainResult<NotificationSettingStateResponseDto, NotificationStateDomainModel> { it.toDomainModel() }
    }

    override suspend fun getRunningRecord(
        userId: Int,
        startDate: Long,
        endDate: Long
    ): NetworkResult<RunningRecordDomainModel> {
        val response =  handleApi {
            userApiService.getRunningRecord(
                userId,
                startDate,
                endDate
            )
        }.toDomainResult<RunningRecordResponseDto, RunningRecordDomainModel> { it.toDomainModel() }
        Log.d(TAG, "getRunningRecord: $response")
        return response
    }

    override suspend fun getHomeData(userId: Int): NetworkResult<HomeDataDomainModel> {
        val response =
            handleApi { userApiService.getHomeData(userId) }.toDomainResult<HomeDataResponseDto, HomeDataDomainModel> { it.toDomainModel() }
        return response
    }

    override suspend fun getFriendRequestList(userId: Int): NetworkResult<List<NotificationListDomainModel>> {
        return handleApi { userApiService.getFriendRequestList(userId) }.toDomainResult<List<NotificationListResponseDto>, List<NotificationListDomainModel>> { it.map { model -> model.toDomainModel() } }
    }

    override suspend fun getRunningRequestList(userId: Int): NetworkResult<List<RunningRequestDomainModel>> {
        return handleApi { userApiService.getRunningRequestList(userId) }.toDomainResult<List<RunningRequestResponse>, List<RunningRequestDomainModel>> { it.map { model -> model.toDomainModel() } }
    }

    override suspend fun getMatchingHistoryList(userId: Int): NetworkResult<MatchingHistoryDomainModel> {
        val response = handleApi { userApiService.getMatchingHistoryList(userId) }.toDomainResult<MatchingHistoryResponseDto, MatchingHistoryDomainModel> { model -> model.toDomain()}
        Log.d(TAG, "getMatchingHistoryList: $response")
        return response
    }

    override suspend fun searchUsers(nickname: String): NetworkResult<List<UserModel>> {
        val response =  handleApi { userApiService.searchUsers(nickname) }
            .toDomainResult<List<UserResponseDto>, List<UserModel>> { response -> response.map { it.toDomainModel() } }
        Log.d(TAG, "searchUsers: $response")
        return response
    }

    override suspend fun getAccessToken(): String {
        return userDataStorePreferences.getAccessToken() ?: ""    }

    override suspend fun getFriendRecommendList(userId: Int): NetworkResult<List<FriendRecommendDomainModel>> {
        val response = handleApi { userApiService.getFriendRecommendList(userId) }.toDomainResult<List<FriendRecommendListResponseDto>, List<FriendRecommendDomainModel>> { it.map { model -> model.toDomainModel() } }
        Log.d(TAG, "getFriendRecommendList: $response")
        return response
    }


}