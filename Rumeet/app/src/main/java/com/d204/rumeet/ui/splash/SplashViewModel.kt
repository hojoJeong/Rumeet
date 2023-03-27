package com.d204.rumeet.ui.splash

import com.d204.rumeet.domain.usecase.auth.GetUserAutoLoginUseCase
import com.d204.rumeet.domain.usecase.user.GetUserFirstAccessCheckUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserFirstAccessCheckUseCase: GetUserFirstAccessCheckUseCase,
    private val getUserAutoLoginUseCase: GetUserAutoLoginUseCase
) : BaseViewModel() {

    // StateFlow는 상태값, 대신 초기값이 필요(데이터에 대한 값들)
    private val _splashScreenGone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val splashScreenGone: StateFlow<Boolean> get() = _splashScreenGone.asStateFlow()

    // SharedFlow는 이벤트에 대한 처리 (화면이동, 다이얼로그 표시)
    private val _navigationEvent: MutableSharedFlow<SplashNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<SplashNavigationAction> get() = _navigationEvent.asSharedFlow()

    /**
     * 첫 실행인지 state를 가져온다. true면 이미 방문했던 것
     * 자동 로그인 체크를 확인하고 LoginActivity로 갈지, MainActivity로 갈지 정한다.
     * RefreshToken이 살아있다면 갱신, 죽었으면 Login으로 간다.(BaseViewModel에 존재함)
     * */
    fun checkAppState() {
        baseViewModelScope.launch {
            launch {
                // true -> 방문했음
                if (getUserFirstAccessCheckUseCase()) {
                    if (getUserAutoLoginUseCase()) _navigationEvent.emit(SplashNavigationAction.StartMainActivity)
                    else _navigationEvent.emit(SplashNavigationAction.StartLoginActivity)
                } else {
                    _navigationEvent.emit(SplashNavigationAction.NavigateOnBoarding)
                }
            }
        }
    }
}