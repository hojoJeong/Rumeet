package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.content.ContentValues
import android.util.Log
import com.d204.rumeet.data.remote.api.RunningApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.running.RunningInfoRequestDto
import com.d204.rumeet.data.remote.dto.response.running.RunningSoloResponseDto
import com.d204.rumeet.data.remote.dto.response.running.toDomain
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import com.d204.rumeet.data.remote.dto.response.user.UserInfoResponse
import com.d204.rumeet.data.remote.dto.response.user.toDomainModel
import com.d204.rumeet.data.remote.mapper.toDomainModel
import com.d204.rumeet.data.remote.dto.request.running.RunningMatchingWithFriendRequestDto
import com.d204.rumeet.data.util.getPolylineMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.friend.FriendModel
import com.d204.rumeet.domain.model.user.RunningSoloDomainModel
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import com.d204.rumeet.domain.repository.RunningRepository
import com.d204.rumeet.domain.toDomainResult
import java.io.File
import javax.inject.Inject

internal class RunningRepositoryImpl @Inject constructor(
    private val runningApiService: RunningApiService
) : RunningRepository{
    override suspend fun recordRunning(
        userId: Int,
        raceId: Int,
        mode: Int,
        velocity: Float,
        time: Int,
        heartRate: Int,
        success: Int,
        polyline: String
    ): NetworkResult<Unit?> {
        try {
            val request = RunningInfoRequestDto(userId, raceId, mode, velocity.toDouble(), time, heartRate, success, polyline)
            val response2 = runningApiService.recordRace(request)
        }catch (e : Exception){
            Log.e("record error", "recordRunning: ${e.message}", )
        }
        return NetworkResult.Success(null)
    }

    override suspend fun startSolo(userId: Int, mode: Int, ghost: Int): NetworkResult<RunningSoloDomainModel> {
        val response = handleApi { runningApiService.startSoloRace(userId, mode, ghost) }.toDomainResult<RunningSoloResponseDto, RunningSoloDomainModel> { it.toDomain()}
        Log.d("러밋_TAG", "startSolo: $response")
        return response
    }

    override suspend fun acceptRunningRequest(raceId: Int): Boolean {
        val response =  runningApiService.acceptRunningRequest(raceId).flag == "success"
        Log.d(TAG, "acceptRunningRequest: $response")
        return response
    }

    override suspend fun denyRunningRequest(raceId: Int): Boolean {
        return runningApiService.denyRunningRequest(raceId).flag == "success"
    }

    override suspend fun inviteRunning(
        userId: Int,
        partnerId: Int,
        mode: Int,
        date: Long
    ): NetworkResult<Int> {
        val request = RunningMatchingWithFriendRequestDto(date = date, mode = mode, partnerId = partnerId, userId = userId)
        val response = handleApi { runningApiService.inviteRunning(request) }.toDomainResult<Int, Int> { it }
        Log.d(TAG, "inviteRunning: $response")
        return response
    }
}