package com.d204.rumeet.data.remote.dto.response

import com.google.gson.annotations.SerializedName

internal data class BaseResponse<T>(
    @SerializedName("flag")
    val flag : String? = null,
    @SerializedName("msg")
    val msg : String? = null,
    @SerializedName("code")
    val code : Int = -1,
    @SerializedName("data")
    val data : T? = null
)

internal data class ErrorResponse(
    @SerializedName("flag")
    val flag : String? = null,
    @SerializedName("msg")
    val msg : String? = null,
    @SerializedName("code")
    val code : Int = -1,
    @SerializedName("data")
    val data : Unit? = null
)