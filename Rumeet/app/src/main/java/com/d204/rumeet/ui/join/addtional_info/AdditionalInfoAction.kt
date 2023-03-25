package com.d204.rumeet.ui.join.addtional_info

sealed class AdditionalInfoAction {
    object EmailSignUp : AdditionalInfoAction()
    object SocialSignUp : AdditionalInfoAction()
    object SignUpSuccess : AdditionalInfoAction()
}