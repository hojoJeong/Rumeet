package com.d204.rumeet.data.remote.interceptor

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import com.d204.rumeet.data.remote.api.AuthApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.auth.RefreshTokenRequestDto
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/*
* 인터셉터는 서버 통신 직전, 직후에 가로채서 추가 작업을 해줄 수 있다.
* 토큰 사용 시, accessToken이 유효한지 확인
* 토큰 만료 시, refreshToken으로 재발급 요청
* */
internal class BearerInterceptor @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDataStorePreferences: UserDataStorePreferences
) : Interceptor {
    // 어노테이션을 통해 예외처리 해야함을 알림
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = ""
        val request = chain.request()
        val response = chain.proceed(request)

        // 401이면 토큰 재발급 후 새로 요청보내기
        if (response.code == 401) {
            runBlocking {
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
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", accessToken).build()
            return chain.proceed(newRequest)
        } else {
            return response
        }
    }
}
