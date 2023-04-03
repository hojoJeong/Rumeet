package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.model.user.ModifyUserDetailInfoDomainModel
import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class ModifyUserDetailInfoUseCase @Inject constructor(private val userRepository: UserRepository){
    suspend operator fun invoke(userInfo: ModifyUserDetailInfoDomainModel) = userRepository.modifyUserDetailInfo(userInfo)
}