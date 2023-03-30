package com.d204.rumeet.ui.running.matching

sealed class RunningMatchingSideEffect{
    object FailMatching : RunningMatchingSideEffect()
    class SuccessMatching(val userId : Int, val roomId : Int, val partnerId : Int) : RunningMatchingSideEffect()
}
