package com.d204.rumeet.domain.repository

interface RunningRepository {
    suspend fun startRunning(mode : Int)
}