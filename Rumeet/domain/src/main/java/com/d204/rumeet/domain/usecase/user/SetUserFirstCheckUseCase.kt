package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class SetUserFirstCheckUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.setUserFirstRunCheck()
}