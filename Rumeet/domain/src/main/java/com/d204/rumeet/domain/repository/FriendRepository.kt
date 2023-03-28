package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendModel

interface FriendRepository {
    suspend fun getUserFriendList() : NetworkResult<List<FriendModel>>
    suspend fun getFriendInfo(friendId : Int) : NetworkResult<FriendModel>
    suspend fun requestFriend(myId : Int, friendId: Int) : NetworkResult<Unit?>
    suspend fun searchFriends(userId : Int, searchNickname : String) :  NetworkResult<List<FriendModel>>
}