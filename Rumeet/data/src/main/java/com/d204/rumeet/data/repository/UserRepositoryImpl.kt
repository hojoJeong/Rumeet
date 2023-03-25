package com.d204.rumeet.data.repository

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.UserApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.user.JoinRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.util.getMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.repository.UserRepository
import okhttp3.MultipartBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val userDataStorePreferences: UserDataStorePreferences
) : UserRepository {

    // SP 관련 예외는 후순위
    override suspend fun setUserFirstRunCheck(): Boolean {
        try {
            userDataStorePreferences.setFirstRun(true)
        } catch (e: IOException) {
            return false
        }
        return true
    }

    override suspend fun getUserFirstRunCheck(): Boolean {
        return userDataStorePreferences.getFirstRun()
    }

    override suspend fun checkDuplicateInfo(type: Int, value: String): NetworkResult<Unit?> {
        return handleApi { userApiService.checkDuplicateInfo(type, value) }
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
        return handleApi { userApiService.join(request, multipartData) }
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
        return handleApi { userApiService.socialJoin(request,profileImg) }
    }
}