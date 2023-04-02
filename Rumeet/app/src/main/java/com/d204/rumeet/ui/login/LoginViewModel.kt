package com.d204.rumeet.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.remote.dto.SocialLoginErrorException
import com.d204.rumeet.data.remote.dto.NoUserFindErrorException
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.auth.DoEmailLoginUseCase
import com.d204.rumeet.domain.usecase.auth.DoKakaoLoginUseCase
import com.d204.rumeet.domain.usecase.auth.RedirectKakaoLoginUseCase
import com.d204.rumeet.domain.usecase.auth.SetUserAutoLoginCheck
import com.d204.rumeet.domain.usecase.user.SetUserTokenUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// Todo 네이버 로그인 승인되면 구현
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val doEmailLoginUseCase: DoEmailLoginUseCase,
    private val doKakaoLoginUseCase: DoKakaoLoginUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase,
    private val redirectKakaoLoginUseCase: RedirectKakaoLoginUseCase,
    private val setUserAutoLoginCheck: SetUserAutoLoginCheck
) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<LoginNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<LoginNavigationAction> = _navigationEvent.asSharedFlow()

    /**
     * 카카오 소셜 로그인
     * 자동로그인은 true로 고정
     * 예외에서 회원가입이 필요한 로그인 로직이라면 다시 요청을 보냄
     * @param accessToken - 카카오 OAuthToken에서 받은 AccessToken
     * */
    fun doKakaoLogin(accessToken : String) {
        baseViewModelScope.launch {
            showLoading()
            doKakaoLoginUseCase(accessToken)
                .onSuccess { jwt ->
                    // navigate 관련 로직이라면 모든 로직 처리 후 navigate 로직 처리, viewModel 파괴 되면 안됨(activity -> activity)
                    setUserAutoLoginCheck(true)
                    Log.d(TAG, "doKakaoLogin: ${jwt.userId}")
                    setUserTokenUseCase(jwt.accessToken, jwt.refreshToken, jwt.userId)
                    _navigationEvent.emit(LoginNavigationAction.LoginSuccess)
                }
                .onError { e ->
                    if(e is SocialLoginErrorException) redirectKakaoLogin(accessToken)
                    else catchError(e)
                }
            dismissLoading()
        }
    }

    /**
     * 회원가입이 필요한 카카오 로그인
     * 자동 로그인은 true로 설정
     * @param accessToken - 카카오 OAuthToken에서 받은 AccessToken
     * */
     private fun redirectKakaoLogin(accessToken: String){
        baseViewModelScope.launch {
            showLoading()
            redirectKakaoLoginUseCase(accessToken)
                .onSuccess { oauthInfo ->
                    _navigationEvent.emit(LoginNavigationAction.NeedJoinFirst(oauthInfo.oauth, oauthInfo.profileImg))
                }
                .onError { e -> catchError(e) }
            dismissLoading()
        }
    }


    /**
     * 이메일 계정 로그인
     * 자동 로그인 여부도 가져가야함
     * @param email - 사용자가 입력한 계정
     * @param password - 사용자가 입력한 비밀번호
     * @param autoLoginState - 자동로그인의 체크 상태
     * */
    fun doEmailLogin(email: String, password: String, autoLoginState: Boolean) {
        baseViewModelScope.launch {
            showLoading()
            doEmailLoginUseCase.invoke(email, password, autoLoginState)
                .onSuccess { jwt ->
                    _navigationEvent.emit(LoginNavigationAction.LoginSuccess)
                    setUserAutoLoginCheck(autoLoginState)
                    Log.d(TAG, "doEmailLogin: ${jwt.userId}")
                    setUserTokenUseCase(jwt.accessToken, jwt.refreshToken, jwt.userId)
                }
                .onError { e ->
                    setUserAutoLoginCheck(false)
                    if (e is NoUserFindErrorException) _navigationEvent.emit(LoginNavigationAction.LoginFailed)
                    else catchError(e)
                }
            dismissLoading()
        }
    }

    // 이메일 로그인
    fun navigateEmailLogin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.EmailLogin)
        }
    }

    // 카카오 로그인
    fun navigateKakaoLogin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.KakaoLogin)
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
}