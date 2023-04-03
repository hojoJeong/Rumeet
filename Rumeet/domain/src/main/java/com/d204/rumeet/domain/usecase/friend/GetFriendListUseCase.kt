package com.d204.rumeet.domain.usecase.friend

import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class GetFriendListUseCase @Inject constructor(
    private val friendRepository: FriendRepository
){
    suspend operator fun invoke(type: Int) = friendRepository.getUserFriendList(type)
}