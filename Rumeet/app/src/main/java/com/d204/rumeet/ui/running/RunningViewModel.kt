package com.d204.rumeet.ui.running

import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.running.option.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RunningViewModel : BaseViewModel() {

    val runningTypeModel = RunningTypeModel()

    // state = 1 싱글, state = 2 멀티
    fun setGameType(state: RunningType) {
        runningTypeModel.runningType = state
    }

    // 거리 or 협동의 난이도
    fun setDistance(distance : RunningDistance){
        runningTypeModel.distance = distance
    }

    // 누구와 달릴지
    fun setRunningDetailType(who : RunningDetailType){
        runningTypeModel.runningDetailType = who
    }

    // 난이도
    fun setRunningDifficulty(difficulty: RunningDifficulty){
        runningTypeModel.runningDifficulty = difficulty
    }
}