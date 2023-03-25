package com.d204.rumeet.ui.find_account

import android.os.CountDownTimer
import com.d204.rumeet.R
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.sign.RequestAuthenticationCodeUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindAccountViewModel @Inject constructor(
    private val requestAuthenticationCodeUseCase: RequestAuthenticationCodeUseCase
) : BaseViewModel() {
    private val _findAccountAction: MutableSharedFlow<FindAccountAction> = MutableSharedFlow()
    val findAccountAction: SharedFlow<FindAccountAction> get() = _findAccountAction.asSharedFlow()

    private val _authenticationState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val authenticationState: StateFlow<Boolean> get() = _authenticationState.asStateFlow()

    private var authenticationCode = ""

    fun requestCode(email: String) {
        baseViewModelScope.launch {
            requestAuthenticationCodeUseCase(email)
                .onSuccess {
                    _authenticationState.value = true
                    authenticationCode = it!!
                    _findAccountAction.emit(FindAccountAction.StartAuthentication)
                    startTimer()
                }
                .onError { e -> catchError(e) }
        }
    }

    fun checkAuthenticationCode(code: String) {
        baseViewModelScope.launch {
            if (code == authenticationCode) _findAccountAction.emit(FindAccountAction.SuccessAuthentication)
            else _findAccountAction.emit(FindAccountAction.FailAuthentication)
        }
    }

    fun clickAuthenticationCode(requestState: Boolean) {
        baseViewModelScope.launch {
            if (requestState) _findAccountAction.emit(FindAccountAction.CheckAuthentication)
            else _findAccountAction.emit(FindAccountAction.RequestAuthenticationCode)
        }
    }

    private fun startTimer() {
        object : CountDownTimer(180000, 180000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                authenticationCode = ""
                _findAccountAction.tryEmit(FindAccountAction.TimeOutAuthentication)
            }
        }.start()
    }

}