package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.AuthRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class SetUserTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newAccessToken : String, newRefreshToken : String, userId : Int) = authRepository.setUserToken(newAccessToken, newRefreshToken, userId)
}