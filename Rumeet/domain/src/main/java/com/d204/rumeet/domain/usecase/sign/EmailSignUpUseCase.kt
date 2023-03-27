package com.d204.rumeet.domain.usecase.sign

import com.d204.rumeet.domain.repository.SignRepository
import com.d204.rumeet.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class EmailSignUpUseCase @Inject constructor(
    private val signRepository: SignRepository
) {
    suspend operator fun invoke(
        id: String,
        password: String,
        nickname: String,
        weight: Float,
        height: Float,
        gender: Int,
        age: Int,
        imageUri: File?
    ) = signRepository.signUpEmail(id, password, nickname, weight, height, gender, age, imageUri)
}