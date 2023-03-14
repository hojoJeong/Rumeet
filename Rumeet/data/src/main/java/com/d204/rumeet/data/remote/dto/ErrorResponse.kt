package com.d204.rumeet.data.remote.dto

/* 서버와 논의 후 결정 */
interface ErrorResponse {
    val status : Int?
    val message : String?
    val code : String?
}