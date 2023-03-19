package com.d204.rumeet.ui.splash

import androidx.lifecycle.viewModelScope
import com.d204.rumeet.domain.usecase.user.GetUserAutoLoginUseCase
import com.d204.rumeet.domain.usecase.user.GetUserFirstCheckUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserFirstCheckUseCase: GetUserFirstCheckUseCase,
    private val getUserAutoLoginUseCase: GetUserAutoLoginUseCase
) : BaseViewModel() {

    // StateFlow는 상태값, 대신 초기값이 필요(로그인 유무 등..)
    private val _splashScreenGone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val splashScreenGone: StateFlow<Boolean> get() = _splashScreenGone.asStateFlow()

    // SharedFlow는 이벤트에 대한 결과, 초기값 필요없음()
    private val _navigateToHome: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val navigateToHome: SharedFlow<Boolean> get() = _navigateToHome.asSharedFlow()

    private val _navigateToOnBoarding: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val navigateToOnBoarding: SharedFlow<Boolean> get() = _navigateToOnBoarding.asSharedFlow()

    private val _navigateToLogin: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val navigateToLogin: SharedFlow<Boolean> get() = _navigateToLogin.asSharedFlow()

    // 앱 버전 체크(토큰)
    fun checkAppState() {
        viewModelScope.launch {
            // 첫 실행인지 state를 가져온다.
            // 첫 실행이라면 onBoarding이 켜진다.
            launch(Dispatchers.Main) {

                _navigateToOnBoarding.emit(!getUserFirstCheckUseCase())
            }

            // 자동 로그인이 체크됐는지 파악한다.
            // 자동로그인이면 자동으로 리프레시를 가져와 갱신이 된다.
            // 아니라면 LoginActivity로 이동한다.
            launch {
                if (getUserAutoLoginUseCase()) _navigateToHome.emit(true)
                else _navigateToLogin.emit(true)
            }
        }
    }
}