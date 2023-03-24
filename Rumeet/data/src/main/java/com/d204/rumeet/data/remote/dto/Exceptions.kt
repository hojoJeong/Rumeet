package com.d204.rumeet.data.remote.dto

import java.io.IOException


class ServerNotFoundException(e: Throwable?, msg: String?) : IOException(e) // 서버 못찾음
class InternalServerErrorException(e: Throwable?, msg: String?) : IOException(e) // 서버 내부 에러
class NoUserFindErrorException(e: Throwable?, msg: String?) : IOException(e) // 로그인 유저 찾을 수 없음
class KakaoLoginErrorException(e : Throwable?, msg : String?) : IOException(e) // 카카오 로그인 오류

class HaveNotJwtTokenException(e: Throwable?, code: Int?) : IOException(e) // Jwt 토큰이 없습니다. 100


object ErrorMessage {
    const val EXCEPTION = 0
    const val SERVER_INTERNAL_ERROR_MESSAGE = "SQL 구문 오류입니다."
    const val NO_USER_FIND_ERROR_MESSAGE = "일치하는 회원이 없습니다."
    const val NO_KAKAO_USER_ERROR_MESSAGE = "회원가입"
}