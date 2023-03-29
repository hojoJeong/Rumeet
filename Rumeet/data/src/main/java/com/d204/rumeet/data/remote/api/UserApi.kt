package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.user.ModifyUserDetailInfoRequestDto
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface UserApi {

    @GET("users/{id}")
    suspend fun getUserInfo(@Path("id") id: Int) : BaseResponse<UserInfoResponse>

    @PUT("users")
    suspend fun modifyUserDetailInfo(@Body userInfo: ModifyUserDetailInfoRequestDto) : BaseResponse<Boolean>
}