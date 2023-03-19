package com.d204.rumeet.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.d204.rumeet.domain.usecase.user.GetUserFirstCheckUseCase
import com.d204.rumeet.domain.usecase.user.SetUserFirstCheckUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.flow.SharedFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setUserFirstCheckUseCase: SetUserFirstCheckUseCase
) : BaseViewModel() {

    private val _startToLogin: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val startToLogin: SharedFlow<Boolean> = _startToLogin

    fun setVisitCheck() {
        viewModelScope.launch {
            setUserFirstCheckUseCase()
        }
    }
}