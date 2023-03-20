package com.d204.rumeet.data.remote.interceptor

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/*
* 인터셉터는 서버 통신 직전, 직후에 가로채서 추가 작업을 해줄 수 있다.
* 토큰 사용 시, accessToken이 유효한지 확인
* 토큰 만료 시, refreshToken으로 재발급 요청
* */
class AuthInterceptor @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences
) : Interceptor {
    // 어노테이션을 통해 예외처리 해야함을 알림
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = ""
        val request = chain.request()
        val response = chain.proceed(request)
        return response
    }
}