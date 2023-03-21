package com.d204.rumeet.domain.usecase.auth

import com.d204.rumeet.domain.repository.AuthRepository
import javax.inject.Inject

class DoEmailLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(email : String, password : String, autoLoginState : Boolean) = authRepository.doEmailLogin(email, password, autoLoginState)
}