package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class CheckIdDuplicateUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(type : Int, value : String) = userRepository.checkDuplicateInfo(type, value)
}