package com.d204.rumeet.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.remote.api.RunningApiService
import com.d204.rumeet.data.remote.api.handleApi
import com.d204.rumeet.data.remote.dto.request.running.RunningInfoRequestDto
import com.d204.rumeet.data.util.getPolylineMultipartData
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.repository.RunningRepository
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
    ) : NetworkResult<Unit?> {
        try {
            val request = RunningInfoRequestDto(userId, raceId, mode, velocity.toDouble(), time, heartRate, success, polyline)

            val response1 = runningApiService.putRace(request)
            val response2 = runningApiService.recordRace(request)
        }catch (e : Exception){
            Log.e("record error", "recordRunning: ${e.message}", )
        }
        return NetworkResult.Success(null)
    }

    override suspend fun acceptRunningRequest(raceId: Int): Boolean {
        val response =  runningApiService.acceptRunningRequest(raceId).flag == "success"
        Log.d(TAG, "acceptRunningRequest: $response")
        return response
    }

    override suspend fun denyRunningRequest(raceId: Int): Boolean {
        return runningApiService.denyRunningRequest(raceId).flag == "success"
    }
}