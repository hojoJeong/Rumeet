package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class EmailSignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
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
    ) = userRepository.signUpEmail(id, password, nickname, weight, height, gender, age, imageUri)
}