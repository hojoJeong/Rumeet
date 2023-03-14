package com.d204.rumeet.data.remote.dto

data class ErrorResponseImpl(
    @SerializedName("status")
    override val status: Int?,
    @SerializedName("message")
    override val message: String?,
    @SerializedName("code")
    override val code: String?
) : ErrorResponse