package com.d204.rumeet.ui.join.id

import android.graphics.Paint.Join
import com.d204.rumeet.data.remote.dto.DuplicateInfoException
import com.d204.rumeet.data.remote.dto.SocialLoginErrorException
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.CheckIdDuplicateUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.login.LoginNavigationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinIdViewModel @Inject constructor(
    private val checkIdDuplicateUseCase: CheckIdDuplicateUseCase
) : BaseViewModel() {

    private val _joinIdAction: MutableSharedFlow<JoinIdAction> = MutableSharedFlow()
    val joinIdAction: SharedFlow<JoinIdAction> get() = _joinIdAction.asSharedFlow()

    /**
     * 아이디 중복체크
     * type = 1(닉네임), 2(이메일)
     * @param id - 중복체크하려는 id
     * */
    fun idValidation(id: String) {
        baseViewModelScope.launch {
            checkIdDuplicateUseCase(1, id)
                .onSuccess {
                    _joinIdAction.emit(JoinIdAction.NavigateNicknameFragment(id))
                }
                .onError { e ->
                    if (e is DuplicateInfoException) _joinIdAction.emit(JoinIdAction.IdDuplicate)
                    else catchError(e)
                }
        }
    }

    fun checkIdValidation() {
        baseViewModelScope.launch {
            _joinIdAction.emit(JoinIdAction.CheckIdDuplicate)
        }
    }
}