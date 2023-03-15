package com.d204.rumeet.ui.splash

import androidx.lifecycle.viewModelScope
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SplashViewModel : BaseViewModel() {
    private val _splashScreenGone : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val splashScreenGone : StateFlow<Boolean> = _splashScreenGone.asStateFlow()

    private val _appVersion : MutableStateFlow<Int> = MutableStateFlow(0)
    val appVersion : StateFlow<Int> = _appVersion.asStateFlow()

    private val _navigateToHome : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToHome : StateFlow<Boolean> = _navigateToHome.asStateFlow()

    // 앱 버전 체크(토큰)
    fun checkVersion(){
        viewModelScope.launch {
            // TODO API가 나온다면 바로 처리
/*            versionAPI()
                .onSuccess { _appVersion.emit(it.version) }
                .onError { e -> catchError(e) }*/
        }
    }

    //토큰 갱신
    fun setDeviceToken(token : String){
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