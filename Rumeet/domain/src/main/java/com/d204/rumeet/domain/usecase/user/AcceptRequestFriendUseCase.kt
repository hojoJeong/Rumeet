package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class AcceptRequestFriendUseCase @Inject constructor(private val friendRepository: FriendRepository) {
    suspend operator fun invoke(friendId: Int, myId: Int) = friendRepository.acceptFriendRequest(friendId, myId)
}