package com.d204.rumeet.ui.running.matching

sealed class RunningMatchingSideEffect{
    object FailMatching : RunningMatchingSideEffect()
    object SuccessMatching : RunningMatchingSideEffect()
}
