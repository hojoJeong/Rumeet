package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.user.FcmTokenRequestDto
import com.d204.rumeet.data.remote.dto.request.user.ModifyNickNameRequest
import com.d204.rumeet.data.remote.dto.request.user.ModifyNotificationStateRequestDto
import com.d204.rumeet.data.remote.dto.request.user.ModifyUserDetailInfoRequestDto
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.user.*
import com.d204.rumeet.data.remote.dto.response.user.UserResponseDto
import okhttp3.MultipartBody
import retrofit2.http.*

internal interface UserApiService {
    @GET("users/search")
    suspend fun searchUsers(
        @Query("nickname") nickname : String
    ) : BaseResponse<List<UserResponseDto?>>

    @GET("users/{id}")
    suspend fun getUserInfo(@Path("id") id: Int) : BaseResponse<UserInfoResponse?>


    @PUT("users")
    suspend fun modifyUserDetailInfo(@Body userInfo: ModifyUserDetailInfoRequestDto): BaseResponse<Boolean>

    @DELETE("users/{id}")
    suspend fun withdrawal(@Path("id") id: Int): BaseResponse<Boolean>

    @GET("badge/list/{userId}")
    suspend fun getAcquiredBadgeList(@Path("userId") id: Int): BaseResponse<List<AcquiredBadgeResponse>>

    @Multipart
    @POST("users/modify/profile")
    suspend fun modifyProfileAndNickName(
        @Part("user") request: ModifyNickNameRequest,
        @Part profileImg : MultipartBody.Part?
    ): BaseResponse<Boolean>


    @POST("fcm/token")
    suspend fun registFcmToken(@Body token: FcmTokenRequestDto): BaseResponse<Boolean>

    @GET("users/alarm/{userId}")
    suspend fun getNotificationSettingState(@Path("userId") userId: Int): BaseResponse<NotificationSettingStateResponseDto>

    @PUT("users/alarm")
    suspend fun modifyNotificationSettingState(@Body requestDto: ModifyNotificationStateRequestDto): BaseResponse<Boolean>

    @GET("record/race/{userId}/{startDate}/{endDate}")
    suspend fun getRunningRecord(@Path("userId") userId: Int, @Path("startDate") startDate: Long, @Path("endDate") endDate: Long): BaseResponse<RunningRecordResponseDto>

    @GET("record/{userId}")
    suspend fun getHomeData(@Path("userId") userId: Int) : BaseResponse<HomeDataResponseDto>

    @GET("friends/to-request")
    suspend fun getFriendRequestList(@Query("userId") userId: Int): BaseResponse<List<NotificationListResponseDto>>

    @GET("game/invite/{userId}")
    suspend fun getRunningRequestList(@Path("userId") userId: Int): BaseResponse<List<RunningRequestResponse>>

    @GET("record/match/{userId}")
    suspend fun getMatchingHistoryList(@Path("userId") userId: Int): BaseResponse<MatchingHistoryResponseDto>
}

