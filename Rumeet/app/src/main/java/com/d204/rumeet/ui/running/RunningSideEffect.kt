package com.d204.rumeet.ui.running

import com.d204.rumeet.domain.model.user.RunningSoloDomainModel
import com.d204.rumeet.domain.model.user.UserInfoDomainModel

sealed class RunningSideEffect{
    class SuccessRunning(val distance : Int) : RunningSideEffect()
    object EndRunning : RunningSideEffect()
    class SuccessUserInfo(val userInfo : UserInfoDomainModel) : RunningSideEffect()
    class SuccessPartnerInfo(val partnerInfo : UserInfoDomainModel) : RunningSideEffect()
    class SuccessSoloData(val data : RunningSoloDomainModel) : RunningSideEffect()
}
