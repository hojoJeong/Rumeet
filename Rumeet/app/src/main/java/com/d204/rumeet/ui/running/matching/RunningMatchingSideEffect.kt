package com.d204.rumeet.ui.running.matching

import com.d204.rumeet.domain.model.user.RunningSoloDomainModel

sealed class RunningMatchingSideEffect{
    object FailMatching : RunningMatchingSideEffect()
    class SuccessMatching(val userId : Int, val roomId : Int, val partnerId : Int) : RunningMatchingSideEffect()
    class SuccessGhostData(val data : RunningSoloDomainModel) : RunningMatchingSideEffect()
}
