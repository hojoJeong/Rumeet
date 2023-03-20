package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class SetUserFirstAccessCheckUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() : Boolean = userRepository.setUserFirstRunCheck()
}