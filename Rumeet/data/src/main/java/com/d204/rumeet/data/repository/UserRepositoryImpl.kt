package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.SignApiService
import com.d204.rumeet.data.remote.api.UserApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.user.JoinRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import com.d204.rumeet.data.remote.dto.response.user.toDomainModel
import com.d204.rumeet.data.util.getMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.FriendModel
import com.d204.rumeet.domain.repository.UserRepository
import com.d204.rumeet.domain.toDomainResult
import java.io.File
import java.io.IOException
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
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

    override suspend fun getUserFriendList(): NetworkResult<List<FriendModel>> {
        return handleApi { userApiService.getFriendList(userDataStorePreferences.getUserId()) }
            .toDomainResult<List<FriendResponseDto>, List<FriendModel>> { response ->
                response
                    .map {
                        it.toDomainModel()
                    }
            }
    }

    override suspend fun getFriendInfo(friendId: Int): NetworkResult<FriendModel> {
        return handleApi { userApiService.getFriendInfo(friendId) }.toDomainResult<FriendResponseDto, FriendModel> { it.toDomainModel() }
    }

}