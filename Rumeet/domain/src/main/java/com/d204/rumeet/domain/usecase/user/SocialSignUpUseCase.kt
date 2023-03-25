package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class SocialSignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        oAuth: Long,
        nickname: String,
        profileImgUrl: String,
        weight: Float,
        height: Float,
        gender: Int,
        age: Int,
        image : File?
    ) = userRepository.signUpSocialLogin(oAuth, nickname, profileImgUrl, weight, height, gender, age, image)
}