package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.ErrorMessage
import com.d204.rumeet.data.remote.dto.ErrorMessage.EXCEPTION
import com.d204.rumeet.data.remote.dto.ErrorMessage.NO_USER_FIND_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.InternalServerErrorException
import com.d204.rumeet.data.remote.dto.NoUserFindErrorException
import com.d204.rumeet.data.remote.dto.ServerNotFoundException
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.domain.NetworkResult


internal inline fun <T> handleApi(transform: () -> BaseResponse<T>) = try {
    val result = transform.invoke()
    // 서버 결과는 성공했지만 fail이라면 예외처리
    if(result.flag == EXCEPTION) throw Exception(result.msg)
    NetworkResult.Success(result.data)
}catch (e : Exception){
    when(e.message) {
        NO_USER_FIND_ERROR_MESSAGE -> NetworkResult.Error(NoUserFindErrorException(e.cause, e.message))
        SERVER_INTERNAL_ERROR_MESSAGE -> NetworkResult.Error(ServerNotFoundException(e.cause, e.message))
        else -> NetworkResult.Error(e)
    }
}