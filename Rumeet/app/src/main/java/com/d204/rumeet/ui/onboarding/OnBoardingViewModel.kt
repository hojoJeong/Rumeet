package com.d204.rumeet.ui.onboarding

import com.d204.rumeet.domain.usecase.user.GetUserFirstCheckUseCase
import com.d204.rumeet.domain.usecase.user.SetUserFirstCheckUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val getUserFirstCheckUseCase: GetUserFirstCheckUseCase,
    private val setUserFirstCheckUseCase: SetUserFirstCheckUseCase
) : BaseViewModel() {

    private val _navigateToLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin

    fun setVisitCheck() {
        baseViewModelScope.launch {
            launch {
                setUserFirstCheckUseCase()
            }

            launch {
                if (getUserFirstCheckUseCase()) _navigateToLogin.emit(true)
            }
        }
    }
}