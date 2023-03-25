package com.d204.rumeet.ui.find_account

sealed class FindAccountAction{
    object RequestAuthenticationCode : FindAccountAction()
    object FailRequestAuthenticationCode : FindAccountAction()
    object TimeOutAuthentication : FindAccountAction()
    object SuccessAuthentication : FindAccountAction()
    object FailAuthentication : FindAccountAction()
    object CheckAuthentication : FindAccountAction()
    object StartAuthentication: FindAccountAction()
}
