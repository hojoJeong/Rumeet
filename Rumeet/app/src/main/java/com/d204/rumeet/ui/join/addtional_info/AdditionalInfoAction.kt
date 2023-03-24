package com.d204.rumeet.ui.join.addtional_info

sealed class AdditionalInfoAction {
    object ShowBodyStateDialog : AdditionalInfoAction()
    object EmailSignUp : AdditionalInfoAction()
    object SocialSignUp : AdditionalInfoAction()
}