package com.d204.rumeet.ui.find_account

import android.os.CountDownTimer
import android.util.Log
import com.d204.rumeet.R
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.sign.RequestAuthenticationCodeUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.util.checkEmailValidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class FindAccountViewModel @Inject constructor(
    private val requestAuthenticationCodeUseCase: RequestAuthenticationCodeUseCase
) : BaseViewModel() {
    private val _findAccountAction: MutableSharedFlow<FindAccountAction> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 10)
    val findAccountAction: SharedFlow<FindAccountAction> get() = _findAccountAction.asSharedFlow()

    private val _authenticationState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val authenticationState: StateFlow<Boolean> get() = _authenticationState.asStateFlow()

    private var authenticationCode = ""
    private lateinit var countDownTimer: CountDownTimer

    fun requestCode(email: String) {
        baseViewModelScope.launch {
            if (checkEmailValidate(email)) {
                requestAuthenticationCodeUseCase(email)
                    .onSuccess {
                        _authenticationState.value = true
                        authenticationCode = it!!
                        _findAccountAction.emit(FindAccountAction.StartAuthentication)
                        startTimer()
                    }
                    .onError { e -> catchError(e) }
            } else _findAccountAction.emit(FindAccountAction.FailRequestAuthenticationCode)
        }
    }

    fun checkAuthenticationCode(code: String) {
        baseViewModelScope.launch {
            if (code == authenticationCode) {
                _findAccountAction.emit(FindAccountAction.SuccessAuthentication)
                countDownTimer.cancel()
            }
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
        baseViewModelScope.launch {
            countDownTimer = object : CountDownTimer(180000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _findAccountAction.tryEmit(FindAccountAction.TimeCheck(millisUntilFinished))
                }

                override fun onFinish() {
                    authenticationCode = ""
                    _authenticationState.tryEmit(false)
                    _findAccountAction.tryEmit(FindAccountAction.TimeOutAuthentication)
                }
            }.start()
        }
    }

}