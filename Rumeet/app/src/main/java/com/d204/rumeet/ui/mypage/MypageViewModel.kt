package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.d204.rumeet.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MypageViewModel : BaseViewModel() {
    private val _navigationEvent: MutableSharedFlow<MyPageAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<MyPageAction> get() = _navigationEvent.asSharedFlow()

    private var _optionList = listOf<String>()
    val optionList: List<String>
        get() = _optionList

    fun navigateSetting(title: String) {
        Log.d(TAG, "setOptionList: 세팅 네비 메소드 호출")
        when (title) {
            optionList[0] -> {
                baseViewModelScope.launch {
                    Log.d(TAG, "setOptionList: 세팅 네비 호출")
                    _navigationEvent.emit(MyPageAction.UserInfo)
                }
            }
        }

    }

    fun setOptionList(list: List<String>){
        _optionList = list
        Log.d(TAG, "setOptionList: 세팅 리스트")
    }
}