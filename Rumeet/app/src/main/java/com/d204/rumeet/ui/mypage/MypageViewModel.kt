package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.model.BadgeContentListUiModel
import com.d204.rumeet.ui.mypage.setting.SettingAction
import com.d204.rumeet.ui.mypage.setting.UserInfoAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MypageViewModel : BaseViewModel() {
    private val _myPageNavigationEvent: MutableSharedFlow<MyPageAction> = MutableSharedFlow()
    val myPageNavigationEvent: SharedFlow<MyPageAction> get() = _myPageNavigationEvent.asSharedFlow()

    private val _settingNavigationEvent: MutableSharedFlow<SettingAction> = MutableSharedFlow()
    val settingNavigationEvent: SharedFlow<SettingAction> get() = _settingNavigationEvent.asSharedFlow()

    private val _userInfonNavigationEvent: MutableSharedFlow<UserInfoAction> = MutableSharedFlow()
    val userInfoNavigationEvent: SharedFlow<UserInfoAction> get() = _userInfonNavigationEvent.asSharedFlow()

    private var _myPageMunuList = listOf<String>()
    val myPageMunuList: List<String>
        get() = _myPageMunuList

    private var _settingOptionList = listOf<String>()
    val settingOptionList: List<String>
        get() = _settingOptionList

    private var _userInfoOptionList = listOf<String>()
    val userInfoOptionList: List<String>
        get() = _userInfoOptionList

    private lateinit var _myBadgeList: BadgeContentListUiModel
    val myBadgeList: BadgeContentListUiModel
        get() = _myBadgeList

    fun setSettingNavigate(title: String) {
        baseViewModelScope.launch {
            when (title) {
                myPageMunuList[0] -> _myPageNavigationEvent.emit(MyPageAction.RunningRecord)
                myPageMunuList[1] -> _myPageNavigationEvent.emit(MyPageAction.MatchingHistory)
                myPageMunuList[2] -> _myPageNavigationEvent.emit(MyPageAction.FriendList)
                myPageMunuList[3] -> _myPageNavigationEvent.emit(MyPageAction.BadgeList)
                myPageMunuList[4] -> _myPageNavigationEvent.emit(MyPageAction.EditProfile)
                myPageMunuList[5] -> _myPageNavigationEvent.emit(MyPageAction.Setting)
                myPageMunuList[6] -> _myPageNavigationEvent.emit(MyPageAction.LogOut)

                settingOptionList[0] -> _settingNavigationEvent.emit(SettingAction.UserInfo)
                settingOptionList[1] -> _settingNavigationEvent.emit(SettingAction.SettingNotification)
                settingOptionList[3] -> _settingNavigationEvent.emit(SettingAction.Privacy)
                settingOptionList[4] -> _settingNavigationEvent.emit(SettingAction.ServiceTerms)
                settingOptionList[5] -> _settingNavigationEvent.emit(SettingAction.LogOut)

                userInfoOptionList[6] -> _userInfonNavigationEvent.emit(UserInfoAction.ResetDetailInfo)
                userInfoOptionList[7] -> _userInfonNavigationEvent.emit(UserInfoAction.ResetPassword)
                userInfoOptionList[8] -> _userInfonNavigationEvent.emit(UserInfoAction.Withdrawal)
            }
        }
    }

    fun setMyPageMunuTitleList(list: List<String>) {
        _myPageMunuList = list
        Log.d(TAG, "setOptionList: $_myPageMunuList")
    }

    fun setSettingMenuTitleList(list: List<String>) {
        _settingOptionList = list
    }

    fun setUserInfoMenuTitleList(list: List<String>) {
        _userInfoOptionList = list
    }

    fun getBadgeList(){
        //TODO 뱃지 서버통신
    }

    fun setBadgeList(badgeList: BadgeContentListUiModel){
        _myBadgeList = badgeList
    }
}