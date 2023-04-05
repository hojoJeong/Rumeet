package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.RunningSoloDomainModel
import java.io.File

interface RunningRepository {
    // 밀리초 아님
    suspend fun startSolo(userId:Int, mode: Int, ghost: Int): NetworkResult<RunningSoloDomainModel>
    suspend fun recordRunning(userId : Int, raceId : Int, mode : Int, velocity : Float, time : Int, heartRate : Int, success : Int, polyline : String) : NetworkResult<Unit?>
    suspend fun acceptRunningRequest(raceId: Int): Boolean
    suspend fun denyRunningRequest(raceId: Int): Boolean
    suspend fun inviteRunning(userId: Int, partnerId: Int, mode: Int, date: Long): NetworkResult<Int>
}