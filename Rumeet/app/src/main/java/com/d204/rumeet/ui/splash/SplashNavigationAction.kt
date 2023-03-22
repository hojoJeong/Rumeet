package com.d204.rumeet.ui.splash

/**
 * 화면에서 발생하는 모든 이벤트
 * class는 파라미터가 필요할 때 사용(it으로 접근 가능)
 * object는 파라미터 없을 때 사용
 * */
sealed class SplashNavigationAction {
    object StartLoginActivity : SplashNavigationAction()
    object StartMainActivity : SplashNavigationAction()
    object NavigateOnBoarding : SplashNavigationAction()
}