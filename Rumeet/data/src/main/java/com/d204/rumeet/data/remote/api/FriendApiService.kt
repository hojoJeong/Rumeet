package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.request.friend.FriendRequestDto
import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface FriendApiService {
    @GET("friends/list/{userId}")
    suspend fun getFriendList(
        @Path("userId") userId : Int
    ) : BaseResponse<List<FriendResponseDto>>

    @GET("friends/{user_id}")
    suspend fun getFriendInfo(
        @Path("user_id") userId: Int
    ) : BaseResponse<FriendResponseDto>

    @POST("friends/request")
    suspend fun requestFriend(
        @Body request : FriendRequestDto
    ) : BaseResponse<Unit?>

    @GET("friends/search")
    suspend fun searchFriend(
        @Query("userId") userId : Int,
        @Query("nickname") nickname : String
    ) : BaseResponse<List<FriendResponseDto>>
}