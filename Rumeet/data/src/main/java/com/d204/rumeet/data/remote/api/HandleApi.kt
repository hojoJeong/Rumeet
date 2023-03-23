package com.d204.rumeet.data.remote.api

import android.util.Log
import com.d204.rumeet.data.remote.dto.ErrorMessage.EXCEPTION
import com.d204.rumeet.data.remote.dto.ErrorMessage.NO_KAKAO_USER_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.NO_USER_FIND_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.InternalServerErrorException
import com.d204.rumeet.data.remote.dto.KakaoLoginErrorException
import com.d204.rumeet.data.remote.dto.NoUserFindErrorException
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.domain.NetworkResult


internal inline fun <T> handleApi(transform: () -> BaseResponse<T>): NetworkResult<T?> {
    val result = transform.invoke()
    return try {
        if (result.code > EXCEPTION) throw Exception(result.msg)
        NetworkResult.Success(result.data)
    } catch (e: Exception) {
        when (e.message) {
            NO_USER_FIND_ERROR_MESSAGE -> NetworkResult.Error(NoUserFindErrorException(e.cause, e.message))
            SERVER_INTERNAL_ERROR_MESSAGE -> NetworkResult.Error(InternalServerErrorException(e.cause, e.message))
            // 카카오 로그인 정상 작동했지만, 회원가입을 진행해야함
            NO_KAKAO_USER_ERROR_MESSAGE -> NetworkResult.Error(KakaoLoginErrorException(e.cause, e.message))
            else -> NetworkResult.Error(e)
        }
    }
}