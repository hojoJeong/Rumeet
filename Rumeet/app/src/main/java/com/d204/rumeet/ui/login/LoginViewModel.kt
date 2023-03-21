package com.d204.rumeet.ui.login

import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.auth.DoEmailLoginUseCase
import com.d204.rumeet.domain.usecase.auth.DoKakaoLoginUseCase
import com.d204.rumeet.domain.usecase.user.SetUserTokenUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// Todo 네이버 로그인 승인되면 구현
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val doEmailLoginUseCase: DoEmailLoginUseCase,
    private val doKakaoLoginUseCase: DoKakaoLoginUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase
) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<LoginNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<LoginNavigationAction> = _navigationEvent.asSharedFlow()

    /**
     * 카카오 소셜 로그인
     * 자동로그인은 true로 고정
     * @param kakaoAccessToken - 카카오에서 받은 OAuthToken 객체의 accessToken
     * */
    fun kakaoLogin(kakaoAccessToken: String) {

    }

    /**
     * 이메일 계정 로그인
     * 자동 로그인 여부도 가져가야함
     * @param email - 사용자가 입력한 계정
     * @param password - 사용자가 입력한 비밀번호
     * @param autoLoginState - 자동로그인의 체크 상태
     * */
    fun login(email: String, password: String, autoLoginState: Boolean) {
        baseViewModelScope.launch {
            doEmailLoginUseCase.invoke(email, password, autoLoginState)
                .onSuccess {
                    _navigationEvent.emit(LoginNavigationAction.StartMainActivity)
                    setUserTokenUseCase.invoke(it.accessToken, it.refreshToken)
                }
                .onError { e -> catchError(e) }
        }
    }

    // 계정찾기로 이동
    fun navigateFindAccount() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.NavigateFindAccount)
        }
    }

    // 회원가입으로 이동
    fun navigateJoin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.NavigateJoin)
        }
    }

    fun emailLogin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.EmailLogin)
        }
    }
}