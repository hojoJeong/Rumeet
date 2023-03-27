package com.d204.rumeet.domain.usecase.sign

import com.d204.rumeet.domain.repository.SignRepository
import com.d204.rumeet.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject
import kotlin.math.sign

class SocialSignUpUseCase @Inject constructor(
    private val signRepository: SignRepository
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
    ) = signRepository.signUpSocialLogin(oAuth, nickname, profileImgUrl, weight, height, gender, age, image)
}