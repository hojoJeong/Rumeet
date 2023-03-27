package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.FriendModel
import java.io.File

interface UserRepository {
    suspend fun setUserFirstRunCheck(): Boolean
    suspend fun getUserFirstRunCheck(): Boolean
    suspend fun getUserFriendList() : NetworkResult<List<FriendModel>>
    suspend fun getFriendInfo(friendId : Int) : NetworkResult<FriendModel>
}