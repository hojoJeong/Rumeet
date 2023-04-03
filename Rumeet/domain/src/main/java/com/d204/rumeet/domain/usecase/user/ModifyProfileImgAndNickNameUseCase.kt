package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.model.user.ModifyProfileAndNickNameDomainModel
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class ModifyProfileImgAndNickNameUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(profile: ModifyProfileAndNickNameDomainModel) = userRepository.modifyProfileImgAndNickName(profile)
}