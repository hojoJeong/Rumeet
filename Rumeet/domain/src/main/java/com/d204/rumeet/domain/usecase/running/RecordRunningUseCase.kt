package com.d204.rumeet.domain.usecase.running

import com.d204.rumeet.domain.repository.RunningRepository
import java.io.File
import javax.inject.Inject

class RecordRunningUseCase @Inject constructor(
    private val runningRepository: RunningRepository
) {
    suspend operator fun invoke(
        userId: Int,
        raceId: Int,
        mode: Int,
        velocity: Float,
        time: Int,
        heartRate: Int,
        success: Int,
        polyline: File?
    ) = runningRepository.recordRunning(
        userId,
        raceId,
        mode,
        velocity,
        time,
        heartRate,
        success,
        polyline
    )
}