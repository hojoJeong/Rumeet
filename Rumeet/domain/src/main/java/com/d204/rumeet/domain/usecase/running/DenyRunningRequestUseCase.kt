package com.d204.rumeet.domain.usecase.running

import com.d204.rumeet.domain.repository.RunningRepository
import javax.inject.Inject

class DenyRunningRequestUseCase @Inject constructor(private val runningRepository: RunningRepository) {
    suspend operator fun invoke(raceId: Int) = runningRepository.denyRunningRequest(raceId)
}