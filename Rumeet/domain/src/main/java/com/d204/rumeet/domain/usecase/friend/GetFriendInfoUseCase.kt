package com.d204.rumeet.domain.usecase.friend

import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class GetFriendInfoUseCase @Inject constructor(
    private val friendRepository: FriendRepository
){
    suspend operator fun invoke(friendId : Int) = friendRepository.getFriendInfo(friendId)
}