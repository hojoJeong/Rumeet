package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.FriendRepository
import javax.inject.Inject

class GetFriendDetailInfoUseCase @Inject constructor(private val friendUseCase: FriendRepository) {
    suspend operator fun invoke(id: Int) = friendUseCase.getFriendDetailInfo(id)
}