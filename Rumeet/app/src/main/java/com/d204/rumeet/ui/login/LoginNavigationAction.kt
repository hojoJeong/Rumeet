package com.d204.rumeet.ui.login

import android.util.Log

sealed class LoginNavigationAction {
    object NavigateFindAccount : LoginNavigationAction()
    object EmailLogin : LoginNavigationAction()
    object KakaoLogin : LoginNavigationAction()
    object NaverLogin : LoginNavigationAction()
    object NavigateJoin : LoginNavigationAction()
    object StartMainActivity : LoginNavigationAction()
    object LoginFailed : LoginNavigationAction()
}