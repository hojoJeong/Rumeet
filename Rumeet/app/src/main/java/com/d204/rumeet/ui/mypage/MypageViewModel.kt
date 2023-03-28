package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.domain.usecase.friend.GetFriendInfoUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.setting.SettingAction
import com.d204.rumeet.ui.mypage.setting.UserInfoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val getFriendInfoUseCase: GetFriendInfoUseCase
) : BaseViewModel() {
    private val _myPageNavigationEvent: MutableSharedFlow<MyPageAction> = MutableSharedFlow()
    val myPageNavigationEvent: SharedFlow<MyPageAction> get() = _myPageNavigationEvent.asSharedFlow()

    private val _settingNavigationEvent: MutableSharedFlow<SettingAction> = MutableSharedFlow()
    val settingNavigationEvent: SharedFlow<SettingAction> get() = _settingNavigationEvent.asSharedFlow()

    private val _userInfonNavigationEvent: MutableSharedFlow<UserInfoAction> = MutableSharedFlow()
    val userInfoNavigationEvent: SharedFlow<UserInfoAction> get() = _userInfonNavigationEvent.asSharedFlow()

    private var _optionList = listOf<String>()
    val optionList: List<String>
        get() = _optionList

    private var _userInfoOptionList = listOf<String>()
    val userInfoOptionList: List<String>
        get() = _userInfoOptionList

    fun setSettingNavigate(title: String) {
        baseViewModelScope.launch {
            when (title) {
                optionList[0] -> _settingNavigationEvent.emit(SettingAction.UserInfo)
                optionList[1] -> _settingNavigationEvent.emit(SettingAction.SettingNotification)
                optionList[3] -> _settingNavigationEvent.emit(SettingAction.Privacy)
                optionList[4] -> _settingNavigationEvent.emit(SettingAction.ServiceTerms)
                optionList[5] -> _settingNavigationEvent.emit(SettingAction.LogOut)

                userInfoOptionList[6] -> _userInfonNavigationEvent.emit(UserInfoAction.ResetDetailInfo)
                userInfoOptionList[7] -> _userInfonNavigationEvent.emit(UserInfoAction.ResetPassword)
                userInfoOptionList[8] -> _userInfonNavigationEvent.emit(UserInfoAction.Withdrawal)
            }
        }
    }

    fun test(){
        baseViewModelScope.launch {
            getFriendInfoUseCase(1)
        }
    }

    fun setOptionList(list: List<String>) {
        _optionList = list
    }

    fun setUserInfoOptionTitleList(list: List<String>) {
        _userInfoOptionList = list
        Log.d(TAG, "setOptionList: $userInfoOptionList")
    }
}