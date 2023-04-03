package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.*
import com.d204.rumeet.data.remote.dto.ErrorMessage.ALREADY_FRIEND_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.ALREADY_REQUEST_FRIEND_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.DUPLICATE_USER_INFO_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.EXCEPTION
import com.d204.rumeet.data.remote.dto.ErrorMessage.NO_KAKAO_USER_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.NO_USER_FIND_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.SIGN_UP_FAIL_ERROR_MESSAGE
import com.d204.rumeet.data.remote.dto.ErrorMessage.WITHDRAWAL_USER_LOGIN_ERROR_MESSAGE
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
            NO_KAKAO_USER_ERROR_MESSAGE -> NetworkResult.Error(SocialLoginErrorException(e.cause, e.message))
            DUPLICATE_USER_INFO_ERROR_MESSAGE -> NetworkResult.Error(DuplicateInfoException(e.cause, e.message))
            SIGN_UP_FAIL_ERROR_MESSAGE -> NetworkResult.Error(SingUpErrorException(e.cause, e.message))
            WITHDRAWAL_USER_LOGIN_ERROR_MESSAGE -> NetworkResult.Error(WithdrawalUserLoginErrorException(e.cause, e.message))
            ALREADY_FRIEND_ERROR_MESSAGE -> NetworkResult.Error(AlreadyFriendException(e.cause, e.message))
            ALREADY_REQUEST_FRIEND_ERROR_MESSAGE -> NetworkResult.Error(AlreadyRequestFriendException(e.cause, e.message))
            else -> NetworkResult.Error(e)
        }
    }
}