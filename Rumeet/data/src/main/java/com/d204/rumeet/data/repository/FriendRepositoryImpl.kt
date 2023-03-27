package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.FriendApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import com.d204.rumeet.data.remote.dto.response.user.toDomainModel
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendModel
import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.toDomainResult
import javax.inject.Inject

internal class FriendRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences,
    private val friendApiService : FriendApiService
)  : FriendRepository {

    override suspend fun getUserFriendList(): NetworkResult<List<FriendModel>> {
        return handleApi { friendApiService.getFriendList(userDataStorePreferences.getUserId()) }
            .toDomainResult<List<FriendResponseDto>, List<FriendModel>> { response ->
                response
                    .map {
                        it.toDomainModel()
                    }
            }
    }

    override suspend fun getFriendInfo(friendId: Int): NetworkResult<FriendModel> {
        return handleApi { friendApiService.getFriendInfo(friendId) }.toDomainResult<FriendResponseDto, FriendModel> { it.toDomainModel() }
    }
}