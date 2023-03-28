package com.d204.rumeet.data.remote.dto

import java.io.IOException


class ServerNotFoundException(e: Throwable?, msg: String?) : IOException(e) // 서버 못찾음
class InternalServerErrorException(e: Throwable?, msg: String?) : IOException(e) // 서버 내부 에러
class NoUserFindErrorException(e: Throwable?, msg: String?) : IOException(e) // 로그인 유저 찾을 수 없음
class SocialLoginErrorException(e : Throwable?, msg : String?) : IOException(e) // 소셜 로그인하기 전, 회원가입 필요
class DuplicateInfoException(e : Throwable?, msg : String?) : IOException(e) // 이메일, 닉네임이 중복
class SingUpErrorException(e : Throwable?, msg: String?) : IOException(e)
class AlreadyFriendException(e : Throwable?, msg : String?) : IOException(e)
class AlreadyRequestFriendException(e: Throwable?, msg: String?) : IOException(e)

class HaveNotJwtTokenException(e: Throwable?, code: Int?) : IOException(e) // Jwt 토큰이 없습니다. 100


object ErrorMessage {
    const val EXCEPTION = 0
    const val SERVER_INTERNAL_ERROR_MESSAGE = "SQL 구문 오류입니다."
    const val NO_USER_FIND_ERROR_MESSAGE = "일치하는 회원이 없습니다."
    const val NO_KAKAO_USER_ERROR_MESSAGE = "회원가입"
    const val DUPLICATE_USER_INFO_ERROR_MESSAGE = "중복 되는 이메일/닉네임이 있습니다."
    const val SIGN_UP_FAIL_ERROR_MESSAGE = "회원가입을 실패했습니다."
    const val ALREADY_FRIEND_ERROR_MESSAGE = "이미 친구입니다."
    const val ALREADY_REQUEST_FRIEND_ERROR_MESSAGE = "이미 친구요청을 보낸 상태입니다."
}