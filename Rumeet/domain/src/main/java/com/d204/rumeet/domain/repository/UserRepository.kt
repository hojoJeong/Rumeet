package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.*

interface UserRepository {
    suspend fun setUserFirstRunCheck(): Boolean
    suspend fun getUserFirstRunCheck(): Boolean
    suspend fun getUserId(): Int
    suspend fun getUserInfo(userId: Int): NetworkResult<UserInfoDomainModel>
    suspend fun modifyUserDetailInfo(userInfo: ModifyUserDetailInfoDomainModel): Boolean
    suspend fun withdrawal(userId: Int): Boolean
    suspend fun getAcquiredBadgeList(userId: Int): NetworkResult<List<AcquiredBadgeListDomainModel>>
    suspend fun logout()
    suspend fun modifyProfileImgAndNickName(profile: ModifyProfileAndNickNameDomainModel): Boolean
    suspend fun registFcmToken(userId: Int, token: String): Boolean
    suspend fun modifyNotificationSettingState(userId: Int, target: Int, state: Int): Boolean
    suspend fun getNotificationSettingState(userId: Int): NetworkResult<NotificationStateDomainModel>
    suspend fun getRunningRecord(userId: Int, startDate: Long, endDate: Long): NetworkResult<RunningRecordDomainModel>
    suspend fun getHomeData(userId: Int): NetworkResult<HomeDataDomainModel>
    suspend fun getFriendRequestList(userId: Int): NetworkResult<List<NotificationListDomainModel>>
    suspend fun getRunningRequestList(userId: Int): NetworkResult<List<RunningRequestDomainModel>>
    suspend fun getMatchingHistoryList(userId: Int): NetworkResult<MatchingHistoryDomainModel>
    suspend fun searchUsers(nickname : String) : NetworkResult<List<UserModel>>
    suspend fun getAccessToken() : String

}