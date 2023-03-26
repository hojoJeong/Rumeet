package com.d204.rumeet.ui.reset_password

import androidx.lifecycle.viewModelScope
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.sign.ResetPasswordUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel() {
    private val _resetPasswordAction: MutableSharedFlow<ResetPasswordAction> = MutableSharedFlow()
    val resetPasswordAction: SharedFlow<ResetPasswordAction> get() = _resetPasswordAction.asSharedFlow()

    fun requestResetPassword(email: String, password: String) {
        baseViewModelScope.launch {
            resetPasswordUseCase(email, password)
                .onSuccess { }
                .onError { }
        }
    }

    fun clickResetPasswordButton() {
        viewModelScope.launch {
            _resetPasswordAction.emit(ResetPasswordAction.RequestResetPassword)
        }
    }
}