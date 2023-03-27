package com.d204.rumeet.domain.usecase.auth

import com.d204.rumeet.domain.repository.AuthRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class GetUserAutoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke() = authRepository.getUserAutoLoginCheck()
}