package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.user.JoinRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

internal interface UserApiService {
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
}