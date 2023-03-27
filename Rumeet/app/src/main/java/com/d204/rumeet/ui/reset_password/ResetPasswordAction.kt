package com.d204.rumeet.ui.reset_password

sealed class ResetPasswordAction {
    object RequestResetPassword : ResetPasswordAction()
    object SuccessResetPassword : ResetPasswordAction()
}