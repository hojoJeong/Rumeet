package com.d204.rumeet.ui.onboarding

import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnBoardingViewModel : BaseViewModel() {
    private val _firstRunCheck : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val firstRunCheck : StateFlow<Boolean> = _firstRunCheck.asStateFlow()

    fun setVisitCheck(){
        baseViewModelScope.launch {

        }
    }
}