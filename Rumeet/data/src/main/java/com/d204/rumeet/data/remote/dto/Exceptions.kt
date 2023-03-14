package com.d204.rumeet.data.remote.dto

import java.io.IOException


class ServerNotFoundException(e: Throwable?, val url: String? = null, code: Int?) : IOException(e) // status 404 서버 못찾음
class InternalServerErrorException(e: Throwable?, val url: String? = null, code: Int?) : IOException(e) // status 500 서버 터짐

class HaveNotJwtTokenException(e: Throwable?, val url: String? = null, code: Int?) : IOException(e) // Jwt 토큰이 없습니다. 100
class InvalidJwtTokenException(e: Throwable?, val url: String? = null, code: Int?) : IOException(e) // Jwt가 유효하지 않습니다. 102
class InvalidKakaoAccessTokenException(e: Throwable?, val url: String? = null, code: Int?) : IOException(e) // 카카오 ID 토큰이 유효하지 않습니다. 104