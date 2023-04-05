package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.model.friend.FriendModel

interface FriendRepository {
    suspend fun getUserFriendList(type: Int) : NetworkResult<List<FriendListDomainModel>>
    suspend fun getFriendInfo(friendId : Int) : NetworkResult<FriendModel>
    suspend fun requestFriend(myId : Int, friendId: Int) : NetworkResult<Unit?>
    suspend fun searchFriends(userId : Int, searchNickname : String) :  NetworkResult<List<FriendListDomainModel>>
    suspend fun acceptFriendRequest(friendId: Int, myId: Int): Boolean
    suspend fun rejectFriendRequest(friendId: Int, myId: Int): Boolean
    suspend fun getFriendDetailInfo(id: Int): NetworkResult<FriendInfoDomainModel>
}