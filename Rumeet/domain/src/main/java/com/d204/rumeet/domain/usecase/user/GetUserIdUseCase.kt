package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.AuthRepository
import com.d204.rumeet.domain.repository.FriendRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke() = userRepository.getUserId()
}