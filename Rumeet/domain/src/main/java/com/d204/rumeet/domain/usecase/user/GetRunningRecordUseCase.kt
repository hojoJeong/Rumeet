package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class GetRunningRecordUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Int, startDate: Long, endDate: Long) = userRepository.getRunningRecord(userId, startDate, endDate)
}