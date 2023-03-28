package com.d204.rumeet.data.repository

import android.util.Log
import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.FriendApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.friend.FriendRequestDto
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import com.d204.rumeet.data.remote.dto.response.user.toDomainModel
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendModel
import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.toDomainResult
import javax.inject.Inject

internal class FriendRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences,
    private val friendApiService: FriendApiService
) : FriendRepository {

    override suspend fun getUserFriendList(): NetworkResult<List<FriendModel>> {
        return handleApi { friendApiService.getFriendList(userDataStorePreferences.getUserId()) }
            .toDomainResult<List<FriendResponseDto>, List<FriendModel>> { response ->
                response.map { it.toDomainModel() }
            }
    }

    override suspend fun getFriendInfo(friendId: Int): NetworkResult<FriendModel> {
        return handleApi { friendApiService.getFriendInfo(friendId) }.toDomainResult<FriendResponseDto, FriendModel> { it.toDomainModel() }
    }

    override suspend fun requestFriend(myId: Int, friendId: Int): NetworkResult<Unit?> {
        val request = FriendRequestDto(myId, friendId)
        return handleApi { friendApiService.requestFriend(request) }
    }

    override suspend fun searchFriends(
        userId: Int,
        searchNickname: String
    ): NetworkResult<List<FriendModel>> {
        return handleApi { friendApiService.searchFriend(userId, searchNickname) }.toDomainResult<List<FriendResponseDto>, List<FriendModel>> {response ->
            response.map { it.toDomainModel() }
        }
    }
}