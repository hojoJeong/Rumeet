package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.running.RunningInfoRequestDto
import com.d204.rumeet.data.remote.dto.request.user.SocialJoinRequestDto
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

internal interface RunningApiService {

    @POST("record/race")
    suspend fun recordRace(
        @Body request : RunningInfoRequestDto
    ) : BaseResponse<Unit?>

    @PUT("record")
    suspend fun putRace(
        @Body request : RunningInfoRequestDto
    ) : BaseResponse<Unit?>
}