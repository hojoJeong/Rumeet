package com.d204.rumeet.ui.join

import com.d204.rumeet.data.remote.dto.DuplicateInfoException
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.CheckDuplicateInfoUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.join.id.JoinIdAction
import com.d204.rumeet.ui.join.nickname.JoinNicknameAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val checkDuplicateInfoUseCase: CheckDuplicateInfoUseCase
) : BaseViewModel() {

    private val _joinIdAction: MutableSharedFlow<JoinIdAction> = MutableSharedFlow()
    val joinIdAction: SharedFlow<JoinIdAction> get() = _joinIdAction.asSharedFlow()

    private val _joinNicknameAction: MutableSharedFlow<JoinNicknameAction> = MutableSharedFlow()
    val joinNicknameAction: SharedFlow<JoinNicknameAction> get() = _joinNicknameAction.asSharedFlow()

    val joinInfo: JoinModel = JoinModel()

    /**
     * 아이디 중복체크
     * type = 1(닉네임), 2(이메일)
     * @param id - 중복체크하려는 id
     * */
    fun idValidation(id: String) {
        baseViewModelScope.launch {
            checkDuplicateInfoUseCase(2, id)
                .onSuccess {
                    _joinIdAction.emit(JoinIdAction.NavigateNicknameFragment)
                    joinInfo.id = id
                }
                .onError { e ->
                    if (e is DuplicateInfoException) _joinIdAction.emit(JoinIdAction.IdDuplicate)
                    else catchError(e)
                }
        }
    }

    /**
     * 닉네임 중복체크
     * type = 1(닉네임), 2(이메일)
     * @param nickname - 중복체크하려는 nick
     * */
    fun nicknameValidation(nickname: String) {
        baseViewModelScope.launch {
            checkDuplicateInfoUseCase(1, nickname)
                .onSuccess {
                    joinInfo.nickname = nickname
                    _joinNicknameAction.emit(JoinNicknameAction.NavigateJoinPassword)
                }
                .onError { e ->
                    if (e is DuplicateInfoException) _joinIdAction.emit(JoinIdAction.IdDuplicate)
                    else catchError(e)
                }
        }
    }

    // 닉네임 중복검사
    fun checkNicknameValidation() {
        baseViewModelScope.launch {
            _joinNicknameAction.emit(JoinNicknameAction.CheckNicknameValidation)
        }
    }

    // 아이디 중복검사 하기
    fun checkIdValidation() {
        baseViewModelScope.launch {
            _joinIdAction.emit(JoinIdAction.CheckIdDuplicate)
        }
    }

    // 갤러리 불러오기
    fun navigationToGallery(){
        baseViewModelScope.launch {
            _joinNicknameAction.emit(JoinNicknameAction.NavigateGallery)
        }
    }
}