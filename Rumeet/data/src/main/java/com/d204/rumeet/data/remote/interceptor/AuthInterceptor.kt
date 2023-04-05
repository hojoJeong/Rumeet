package com.d204.rumeet.data.remote.interceptor

import com.d204.rumeet.data.local.datastore.UserDataStorePreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

internal class AuthInterceptor @Inject constructor(
    private val userDataStorePreferences: UserDataStorePreferences
): Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        var jwtToken = ""
        runBlocking {
            userDataStorePreferences.getAccessToken()?.let {
                jwtToken = it
            }
        }

        builder.addHeader("Authorization", jwtToken)
        return chain.proceed(builder.build())
    }
}