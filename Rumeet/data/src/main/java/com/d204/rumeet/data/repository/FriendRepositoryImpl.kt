package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.FriendApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.friend.FriendRequestDto
import com.d204.rumeet.data.remote.dto.response.user.FriendDetailInfoResponseDto
import com.d204.rumeet.data.remote.dto.response.user.FriendListResponseDto
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import com.d204.rumeet.data.remote.dto.response.user.toDomainModel
import com.d204.rumeet.data.remote.mapper.toDomain
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.model.friend.FriendModel
import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.toDomainResult
import javax.inject.Inject

internal class FriendRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences,
    private val friendApiService: FriendApiService
) : FriendRepository {

    override suspend fun getUserFriendList(type: Int): NetworkResult<List<FriendListDomainModel>> {
        val response =  handleApi {
            friendApiService.getFriendList(
                userDataStorePreferences.getUserId(),
                type
            )
        }
            .toDomainResult<List<FriendListResponseDto>, List<FriendListDomainModel>> { response ->
                response.map { it.toDomainModel() }
            }
        Log.d(TAG, "getUserFriendList: $response")
        return response
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
    ): NetworkResult<List<FriendListDomainModel>> {
        val response = handleApi {
            friendApiService.searchFriend(
                userId,
                searchNickname
            )
        }.toDomainResult<List<FriendListResponseDto>, List<FriendListDomainModel>> { response ->
            response.map { it.toDomainModel() }
        }
        Log.d(TAG, "searchFriends: $response")
        return response
    }

    override suspend fun acceptFriendRequest(friendId: Int, myId: Int): Boolean {
        val request = FriendRequestDto(friendId, myId)
        val response = friendApiService.acceptRequestFriend(request).flag == "success"
        return response
    }

    override suspend fun rejectFriendRequest(friendId: Int, myId: Int): Boolean {
        val request = FriendRequestDto(friendId, myId)
        return friendApiService.rejectRequestFriend(request).flag == "success"
    }

    override suspend fun getFriendDetailInfo(id: Int): NetworkResult<FriendInfoDomainModel> {
        val response =
            handleApi { friendApiService.getFriendDetailInfo(id) }.toDomainResult<FriendDetailInfoResponseDto, FriendInfoDomainModel> { it.toDomain() }
        Log.d(TAG, "getFriendDetailInfo: $response")
        return response
    }
}