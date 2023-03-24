package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
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
        age: Int
    ) = userRepository.signUpSocialLogin(oAuth, nickname, profileImgUrl, weight, height, gender, age)
}