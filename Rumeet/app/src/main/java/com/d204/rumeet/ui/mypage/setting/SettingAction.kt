package com.d204.rumeet.ui.mypage.setting

import com.d204.rumeet.ui.mypage.MyPageAction

sealed class SettingAction {
    object UserInfo : SettingAction()
    object SettingNotification : SettingAction()
    object Privacy : SettingAction()
    object ServiceTerms : SettingAction()
    object LogOut : SettingAction()
}