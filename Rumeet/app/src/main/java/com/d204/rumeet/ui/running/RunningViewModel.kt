package com.d204.rumeet.ui.running

import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RunningViewModel: BaseViewModel() {
    private val _runningInfo: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Loading)
    val runningInfoerName: StateFlow<UiState<String>>
        get() = _runningInfo.asStateFlow()
}