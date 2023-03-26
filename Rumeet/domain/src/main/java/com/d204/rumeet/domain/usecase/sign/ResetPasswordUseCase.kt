package com.d204.rumeet.domain.usecase.sign

import com.d204.rumeet.domain.repository.SignRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val signRepository: SignRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        signRepository.resetPassword(email, password)
}