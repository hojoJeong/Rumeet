package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.domain.usecase.user.GetUserInfoUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.model.BadgeContentListUiModel
import com.d204.rumeet.ui.mypage.model.UserInfoUiModel
import com.d204.rumeet.ui.mypage.setting.SettingAction
import com.d204.rumeet.ui.mypage.setting.UserInfoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel(), MyPageEventHandler{
    private val _myPageNavigationEvent: MutableSharedFlow<MyPageAction> = MutableSharedFlow()
    val myPageNavigationEvent: SharedFlow<MyPageAction> get() = _myPageNavigationEvent.asSharedFlow()

    private val _settingNavigationEvent: MutableSharedFlow<SettingAction> = MutableSharedFlow()
    val settingNavigationEvent: SharedFlow<SettingAction> get() = _settingNavigationEvent.asSharedFlow()

    private val _userInfoNavigationEvent: MutableSharedFlow<UserInfoAction> = MutableSharedFlow()
    val userInfoNavigationEvent: SharedFlow<UserInfoAction> get() = _userInfoNavigationEvent.asSharedFlow()

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

    private val _userId: MutableStateFlow<UiState<Int>> = MutableStateFlow(UiState.Loading)
    val userId: StateFlow<UiState<Int>>
        get() = _userId.asStateFlow()

    private val _userInfo: MutableStateFlow<NetworkResult<UserInfoUiModel>> =
        MutableStateFlow(NetworkResult.Loading)
    val userInfo: StateFlow<NetworkResult<UserInfoUiModel>>
        get() = _userInfo.asStateFlow()

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

                userInfoOptionList[6] -> _userInfoNavigationEvent.emit(UserInfoAction.ResetDetailInfo)
                userInfoOptionList[7] -> _userInfoNavigationEvent.emit(UserInfoAction.ResetPassword)
                userInfoOptionList[8] -> _userInfoNavigationEvent.emit(UserInfoAction.Withdrawal)
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

    fun getBadgeList() {
        //TODO 뱃지 서버통신
    }

    fun getUserId() {
        baseViewModelScope.launch {
            try {
                val response = getUserIdUseCase()
                _userId.value = UiState.Success(response)
            } catch (e: Exception) {
                _userId.value = UiState.Error(e.cause)
            }
        }
    }

    fun getUserInfo() {
        baseViewModelScope.launch {
            showLoading()
            Log.d(TAG, "getUserInfo: ${userId.value.successOrNull()}")
            getUserInfoUseCase(userId.value.successOrNull() ?: -1)
                .onSuccess {
                    dismissLoading()
                    Log.d(TAG, "getUserInfo: $it")
                }
                .onError {
                    catchError(it)
                }
        }
    }

    override fun onClick(title: String) {
        setSettingNavigate(title)
    }
}