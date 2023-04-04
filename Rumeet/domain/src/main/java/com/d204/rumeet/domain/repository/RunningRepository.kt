package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import java.io.File

interface RunningRepository {
    // 밀리초 아님
    suspend fun recordRunning(userId : Int, raceId : Int, mode : Int, velocity : Float, time : Int, heartRate : Int, success : Int, polyline : File?) : NetworkResult<Unit?>
    suspend fun acceptRunningRequest(raceId: Int): Boolean
    suspend fun denyRunningRequest(raceId: Int): Boolean
}