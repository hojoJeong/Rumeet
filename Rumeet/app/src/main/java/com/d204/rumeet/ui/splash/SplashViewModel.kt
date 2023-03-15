package com.d204.rumeet.ui.splash

import androidx.lifecycle.viewModelScope
import com.d204.rumeet.domain.usecase.user.GetUserFirstCheckUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserFirstCheckUseCase: GetUserFirstCheckUseCase
) : BaseViewModel() {
    private val _splashScreenGone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val splashScreenGone: StateFlow<Boolean> = _splashScreenGone.asStateFlow()

    private val _appVersion: MutableStateFlow<Int> = MutableStateFlow(0)
    val appVersion: StateFlow<Int> = _appVersion.asStateFlow()

    private val _navigateToHome: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private val _navigateToLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin.asStateFlow()

    // 앱 버전 체크(토큰)
    fun checkVersion() {
        viewModelScope.launch {
            // 첫실행이라면 LoginActivity로 이동
            // LoginActivity로 이동하면서 ViewModel은 사라진다.
            launch {
                if (!getUserFirstCheckUseCase()) _navigateToLogin.emit(true)
            }

            // TODO API가 나온다면 바로 처리
            launch {
/*                versionAPI()
                    .onSuccess { _appVersion.emit(it.version) }
                    .onError { e -> catchError(e) }*/
            }
        }
    }

    //토큰 갱신
    fun setDeviceToken(token: String) {
        // baseViewModelScope = coroutineErrorHandler + viewModelScope, 에러처리가 나면 coroutineErrorHandler에 있는 block이 실행된다.
        baseViewModelScope.launch {
/*            launch {
                val token = getAccessToken()?.let{
                    if(it != ""){
                        usecase(token)
                            .onSuccess{save token}
                            .onError {e -> catchError(e)}
                    }
                }
            }
            // etc...
            launch {

            }*/

            _navigateToHome.emit(true)
            _splashScreenGone.emit(true)
        }
    }
}