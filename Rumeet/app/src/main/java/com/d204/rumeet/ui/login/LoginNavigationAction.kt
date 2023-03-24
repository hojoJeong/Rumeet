package com.d204.rumeet.ui.login

sealed class LoginNavigationAction {
    object NavigateFindAccount : LoginNavigationAction()
    object EmailLogin : LoginNavigationAction()
    object KakaoLogin : LoginNavigationAction()
    object NavigateJoin : LoginNavigationAction()
    object LoginSuccess : LoginNavigationAction()
    object LoginFailed : LoginNavigationAction()
    class NeedJoinFirst(val oauth : Long, val profileImg : String) : LoginNavigationAction()

    // Todo 후순위
    object NaverLogin : LoginNavigationAction()
}