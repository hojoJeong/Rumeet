package com.d204.rumeet.domain.usecase.friend

import com.d204.rumeet.domain.repository.FriendRepository
import javax.inject.Inject

class SearchFriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    suspend operator fun invoke(userId : Int, searchNickname : String) = friendRepository.searchFriends(userId, searchNickname)
}