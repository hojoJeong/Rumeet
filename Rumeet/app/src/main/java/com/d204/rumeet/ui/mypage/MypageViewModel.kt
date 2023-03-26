package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.setting.SettingAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MypageViewModel : BaseViewModel() {
    private val _myPageNavigationEvent: MutableSharedFlow<MyPageAction> = MutableSharedFlow()
    val myPageNavigationEvent: SharedFlow<MyPageAction> get() = _myPageNavigationEvent.asSharedFlow()

    private val _settingNavigationEvent: MutableSharedFlow<SettingAction> = MutableSharedFlow()
    val settingNavigationEvent: SharedFlow<SettingAction> get() = _settingNavigationEvent.asSharedFlow()

    private var _optionList = listOf<String>()
    val optionList: List<String>
        get() = _optionList

    fun setSettingNavigate(title: String) {
        baseViewModelScope.launch {
            when (title) {
                optionList[0] -> _settingNavigationEvent.emit(SettingAction.UserInfo)
                optionList[1] -> _settingNavigationEvent.emit(SettingAction.SettingNotification)
                optionList[3] -> _settingNavigationEvent.emit(SettingAction.Privacy)
                optionList[4] -> _settingNavigationEvent.emit(SettingAction.ServiceTerms)
                optionList[5] -> _settingNavigationEvent.emit(SettingAction.LogOut)
            }
        }


    }

    fun setOptionList(list: List<String>) {
        _optionList = list
        Log.d(TAG, "setOptionList: 세팅 리스트")
    }
}