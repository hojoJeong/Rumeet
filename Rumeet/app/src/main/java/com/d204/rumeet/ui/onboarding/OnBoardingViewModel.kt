package com.d204.rumeet.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.d204.rumeet.domain.usecase.user.SetUserFirstCheckUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setUserFirstCheckUseCase: SetUserFirstCheckUseCase
) : BaseViewModel() {

    private val _startToLogin: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val startToLogin: SharedFlow<Boolean> = _startToLogin.asSharedFlow()

    fun setVisitCheck() {
        baseViewModelScope.launch {
            // firstCheck를 true로만들기 (한번 방문 처리)
            _startToLogin.emit(setUserFirstCheckUseCase())
        }
    }
}
