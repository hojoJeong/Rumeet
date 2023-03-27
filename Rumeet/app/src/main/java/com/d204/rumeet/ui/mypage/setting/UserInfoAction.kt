package com.d204.rumeet.ui.mypage.setting

sealed class UserInfoAction{
    object ResetDetailInfo : UserInfoAction()
    object ResetPassword : UserInfoAction()
    object Withdrawal : UserInfoAction()
}
