 package com.d204.rumeet.data.remote.dto

import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.ErrorResponse
import com.google.gson.Gson
import java.io.IOException

class ServerNotFoundException(e: Throwable?, msg: String?) : IOException(e) // 서버 못찾음
class InternalServerErrorException(e: Throwable?, msg: String?) : IOException(e) // 서버 내부 에러
class NoUserFindErrorException(e: Throwable?, msg: String?) : IOException(e) // 로그인 유저 찾을 수 없음
class SocialLoginErrorException(e : Throwable?, msg : String?) : IOException(e) // 소셜 로그인하기 전, 회원가입 필요
class DuplicateInfoException(e : Throwable?, msg : String?) : IOException(e) // 이메일, 닉네임이 중복
class SingUpErrorException(e : Throwable?, msg: String?) : IOException(e) // 회원가입 오류
class AlreadyFriendException(e : Throwable?, msg : String?) : IOException(e) // 이미 친구
class AlreadyRequestFriendException(e: Throwable?, msg: String?) : IOException(e) // 이미 친구 요청
class WithdrawalUserLoginErrorException(e: Throwable?, msg: String?) : IOException(e) //회원탈퇴한 유저가 가입, 로그인 시도
class HaveNotJwtTokenException(e: Throwable?, code: Int?) : IOException(e) // Jwt 토큰이 없습니다. 100
class ExpiredJwtTokenException(e :Throwable?, msg : String) : IOException(e) // JWT 만료

internal fun createErrorResponse(responseBodyString: String): ErrorResponse =
    Gson().fromJson(responseBodyString, ErrorResponse::class.java)


internal fun createErrorException(
    errorResponse: ErrorResponse
) : Exception? = when(errorResponse.msg) {
    ErrorMessage.SERVER_INTERNAL_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.NO_USER_FIND_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.NO_KAKAO_USER_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.EXPIRED_JWT_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.DUPLICATE_USER_INFO_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.SIGN_UP_FAIL_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.ALREADY_FRIEND_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.ALREADY_REQUEST_FRIEND_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    ErrorMessage.WITHDRAWAL_USER_LOGIN_ERROR_MESSAGE -> ExpiredJwtTokenException(Throwable(errorResponse.msg), errorResponse.msg)
    else -> null
}

object ErrorMessage {
    const val EXCEPTION = 0
    const val SERVER_INTERNAL_ERROR_MESSAGE = "SQL 구문 오류입니다."
    const val NO_USER_FIND_ERROR_MESSAGE = "일치하는 회원이 없습니다."
    const val NO_KAKAO_USER_ERROR_MESSAGE = "회원가입"
    const val EXPIRED_JWT_ERROR_MESSAGE = "JWT 만료되었습니다."
    const val DUPLICATE_USER_INFO_ERROR_MESSAGE = "중복 되는 이메일/닉네임이 있습니다."
    const val SIGN_UP_FAIL_ERROR_MESSAGE = "회원가입을 실패했습니다."
    const val ALREADY_FRIEND_ERROR_MESSAGE = "이미 친구입니다."
    const val ALREADY_REQUEST_FRIEND_ERROR_MESSAGE = "이미 친구요청을 보낸 상태입니다."
    const val WITHDRAWAL_USER_LOGIN_ERROR_MESSAGE = "이미 탈퇴한 회원입니다."
}