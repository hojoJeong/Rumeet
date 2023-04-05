package com.d204.rumeet.domain.usecase.running

import com.d204.rumeet.domain.repository.RunningRepository
import javax.inject.Inject

class InviteRunningUseCase @Inject constructor(private val runningRepository: RunningRepository) {
    suspend operator fun invoke(userId: Int, partnerId: Int, mode: Int, date: Long) = runningRepository.inviteRunning(userId, partnerId, mode, date)
}