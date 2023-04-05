package com.d204.rumeet.data.remote.interceptor

import android.util.Log
import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.AuthApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.ExpiredJwtTokenException
import com.d204.rumeet.data.remote.dto.createErrorException
import com.d204.rumeet.data.remote.dto.createErrorResponse
import com.d204.rumeet.data.remote.dto.request.auth.RefreshTokenRequestDto
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

/*
* 인터셉터는 서버 통신 직전, 직후에 가로채서 추가 작업을 해줄 수 있다.
* 토큰 사용 시, accessToken이 유효한지 확인
* 토큰 만료 시, refreshToken으로 재발급 요청
* */
internal class TokenAuthInterceptor @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDataStorePreferences: UserDataStorePreferences
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        var accessToken = ""
        val errorResponse = response.body?.string().let { createErrorResponse(it!!) }
        val errorException = createErrorException(errorResponse)

        if (response.code == 401 || errorException is ExpiredJwtTokenException) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    with(userDataStorePreferences) {
                        val refreshRequest =
                            RefreshTokenRequestDto(getUserId(), getRefreshToken() ?: "")
                        handleApi { authApiService.getJWTByRefreshToken(refreshRequest) }
                            .onSuccess { response ->
                                this.setUserId(response?.id ?: -1)
                                this.setToken(
                                    response?.accessToken ?: "",
                                    response?.refreshToken ?: ""
                                )
                                accessToken = response?.accessToken ?: ""
                            }
                            .onError {
                                this.clearUserInfo()
                                accessToken = ""
                            }
                    }
                }
            }
            return response.request.newBuilder()
                .header("Authorization", accessToken)
                .build()
        }
        return null
    }
}
