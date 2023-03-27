package com.d204.rumeet.domain.usecase.sign

import com.d204.rumeet.domain.repository.SignRepository
import javax.inject.Inject
import javax.inject.Singleton

class RequestAuthenticationCodeUseCase @Inject constructor(
    private val signRepository: SignRepository
){
    suspend operator fun invoke(email : String) = signRepository.requestAuthenticationCode(email)
}