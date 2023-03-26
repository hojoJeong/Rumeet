package com.d204.rumeet.data.repository

import com.d204.rumeet.data.remote.api.SignApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.sign.ResetPasswordRequestDto
import com.d204.rumeet.data.remote.dto.request.user.JoinRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.util.getMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.repository.SignRepository
import java.io.File
import javax.inject.Inject
import kotlin.math.sign

internal class SignRepositoryImpl @Inject constructor(
    private val signApiService: SignApiService
) : SignRepository {
    override suspend fun checkDuplicateInfo(type: Int, value: String): NetworkResult<Unit?> {
        return handleApi { signApiService.checkDuplicateInfo(type, value) }
    }

    override suspend fun signUpEmail(
        id: String,
        password: String,
        nickname: String,
        weight: Float,
        height: Float,
        gender: Int,
        age: Int,
        imageUri: File?
    ): NetworkResult<Unit?> {
        val multipartData = getMultipartData(imageUri)
        val request = JoinRequestDto(id, password, nickname, gender, age, height, weight, System.currentTimeMillis())
        return handleApi { signApiService.join(request, multipartData) }
    }

    override suspend fun signUpSocialLogin(
        oAuth: Long,
        nickname: String,
        profileImgUrl: String,
        weight: Float,
        height: Float,
        gender: Int,
        age: Int,
        imageUri : File?
    ): NetworkResult<Unit?> {
        // 소셜로그인의 아이디 비밀번호는 oauth로 전달
        val request = SocialJoinRequestDto("", "", nickname, gender, age, height, weight, oAuth.toString(), System.currentTimeMillis())
        val profileImg = getMultipartData(imageUri)
        // 소셜로그인은 서버에서 로직처리, 멀티파트는 null을 전달
        return handleApi { signApiService.socialJoin(request,profileImg) }
    }

    override suspend fun requestAuthenticationCode(email: String): NetworkResult<String?> {
        return handleApi { signApiService.requestAuthenticationCode(email) }
    }

    override suspend fun resetPassword(email: String, password: String): NetworkResult<Unit?> {
        val request = ResetPasswordRequestDto(email, password)
        return handleApi { signApiService.resetPassword(request) }
    }
}