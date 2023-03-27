package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendModel

interface FriendRepository {
    suspend fun getUserFriendList() : NetworkResult<List<FriendModel>>
    suspend fun getFriendInfo(friendId : Int) : NetworkResult<FriendModel>
}