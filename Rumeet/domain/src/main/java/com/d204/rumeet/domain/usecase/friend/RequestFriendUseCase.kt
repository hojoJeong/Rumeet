package com.d204.rumeet.domain.usecase.friend

import com.d204.rumeet.domain.repository.FriendRepository
import javax.inject.Inject

class RequestFriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
){
    suspend operator fun invoke(myId : Int, friendId : Int) = friendRepository.requestFriend(myId, friendId)
}