package com.d204.rumeet.domain.usecase.chatting

import com.d204.rumeet.domain.repository.ChattingRepository
import javax.inject.Inject

class GetChattingRoomUseCase @Inject constructor(
    private val chattingRepository: ChattingRepository
) {
    suspend operator fun invoke(userId : Int) = chattingRepository.getChattingRooms(userId)
}