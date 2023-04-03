package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class RegistFcmTokenUseCase @Inject constructor(private val userRepository: UserRepository){
    suspend operator fun invoke(userId: Int, token: String) = userRepository.registFcmToken(userId, token)
}