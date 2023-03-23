package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface UserApiService {
    @GET("users/check")
    suspend fun checkDuplicateInfo(
        @Query("type") type: Int,
        @Query("value") value : String
    ) : BaseResponse<Unit>
}