package com.d204.rumeet.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseImpl(
    @SerializedName("status")
    override val status: Int?,
    @SerializedName("message")
    override val message: String?,
    @SerializedName("code")
    override val code: String?
) : ErrorResponse