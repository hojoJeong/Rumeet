package com.d204.rumeet.ui.mypage

sealed class EditUserInfoAction {
    object EditUserInfo : EditUserInfoAction()
    object EditPassword : EditUserInfoAction()
}