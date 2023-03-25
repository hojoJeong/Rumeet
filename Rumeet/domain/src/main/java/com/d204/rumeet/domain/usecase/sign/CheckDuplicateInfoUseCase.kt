package com.d204.rumeet.domain.usecase.sign

import com.d204.rumeet.domain.repository.SignRepository
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class CheckDuplicateInfoUseCase @Inject constructor(
    private val signRepository: SignRepository
) {
    suspend operator fun invoke(type : Int, value : String) = signRepository.checkDuplicateInfo(type, value)
}