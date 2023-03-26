package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.sign.ResetPasswordRequestDto
import com.d204.rumeet.data.remote.dto.request.user.JoinRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

internal interface SignApiService {
    @GET("users/check")
    suspend fun checkDuplicateInfo(
        @Query("type") type: Int,
        @Query("value") value : String
    ) : BaseResponse<Unit>

    @Multipart
    @POST("users/join")
    suspend fun join(
        @Part("user") request : JoinRequestDto,
        @Part file : MultipartBody.Part?
    ) : BaseResponse<Unit?>

    @Multipart
    @POST("users/oauth/join")
    suspend fun socialJoin(
        @Part("user") request : SocialJoinRequestDto,
        @Part file : MultipartBody.Part?,
    ) : BaseResponse<Unit?>

    @GET("users/email")
    suspend fun requestAuthenticationCode(
        @Query("email") email : String
    ) : BaseResponse<String?>

    @POST("users/modify/pwd")
    suspend fun resetPassword(
        @Body request : ResetPasswordRequestDto
    ) : BaseResponse<Unit?>
}