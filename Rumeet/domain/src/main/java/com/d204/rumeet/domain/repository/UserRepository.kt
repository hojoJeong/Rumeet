package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.AcquiredBadgeListDomainModel
import com.d204.rumeet.domain.model.user.ModifyProfileAndNickNameDomainModel
import com.d204.rumeet.domain.model.user.ModifyUserDetailInfoDomainModel
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import java.io.File

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
}