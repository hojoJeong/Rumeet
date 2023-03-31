package com.d204.rumeet.ui.running

sealed class RunningSideEffect{
    class SuccessRunning(val distance : Int) : RunningSideEffect()
    class EndRunning() : RunningSideEffect()
}
