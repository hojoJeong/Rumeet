package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.domain.model.user.MatchingHistoryDomainModel
import com.d204.rumeet.domain.model.user.NotificationStateDomainModel
import com.d204.rumeet.domain.model.user.RunningRecordDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.*
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.model.*
import com.d204.rumeet.ui.mypage.setting.SettingAction
import com.d204.rumeet.ui.mypage.setting.UserInfoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
    private val getAcquiredBadgeListUseCase: GetAcquiredBadgeListUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getRunningRecordUseCase: GetRunningRecordUseCase,
    private val getNotificationSettingStateUseCase: GetNotificationSettingStateUseCase,
    private val modifyNotificationSettingStateUseCase: ModifyNotificationSettingStateUseCase,
    private val getMatchingHistoryUseCase: GetMatchingHistoryUseCase
) : BaseViewModel(), MyPageEventHandler {
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

    private val _userInfo: MutableStateFlow<UiState<UserInfoUiModel>> =
        MutableStateFlow(UiState.Loading)
    val userInfo: StateFlow<UiState<UserInfoUiModel>>
        get() = _userInfo.asStateFlow()

    private val _resultWithdrawal: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading)
    val resultWithdrawal: StateFlow<UiState<Boolean>>
        get() = _resultWithdrawal.asStateFlow()

    private val _acquiredBadgeList: MutableStateFlow<UiState<List<AcquiredBadgeUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val acquiredBadgeList: StateFlow<UiState<List<AcquiredBadgeUiModel>>>
        get() = _acquiredBadgeList.asStateFlow()

    private val _runningRecord: MutableStateFlow<UiState<RunningRecordDomainModel>> =
        MutableStateFlow(UiState.Loading)
    val runningRecord: StateFlow<UiState<RunningRecordDomainModel>>
        get() = _runningRecord

    private val _notificationSettingState: MutableStateFlow<UiState<NotificationStateDomainModel>> =
        MutableStateFlow(UiState.Loading)
    val notificationSettingState: StateFlow<UiState<NotificationStateDomainModel>>
        get() = _notificationSettingState.asStateFlow()

    private val _matchingHistoryList: MutableStateFlow<UiState<MatchingHistoryDomainModel>> =
        MutableStateFlow(UiState.Loading)
    val matchingHistoryList: StateFlow<UiState<MatchingHistoryDomainModel>>
        get() = _matchingHistoryList.asStateFlow()

    fun setSettingNavigate(title: String) {
        baseViewModelScope.launch {
            when (title) {
                myPageMunuList[0] -> _myPageNavigationEvent.emit(MyPageAction.RunningRecord)
                myPageMunuList[1] -> _myPageNavigationEvent.emit(MyPageAction.MatchingHistory)
                myPageMunuList[2] -> _myPageNavigationEvent.emit(MyPageAction.FriendList)
                myPageMunuList[3] -> _myPageNavigationEvent.emit(MyPageAction.BadgeList)
                myPageMunuList[4] -> _myPageNavigationEvent.emit(MyPageAction.EditProfile)
                myPageMunuList[5] -> _myPageNavigationEvent.emit(MyPageAction.Setting)

                settingOptionList[0] -> _settingNavigationEvent.emit(SettingAction.UserInfo)
                settingOptionList[1] -> _settingNavigationEvent.emit(SettingAction.SettingNotification)
                settingOptionList[3] -> _settingNavigationEvent.emit(SettingAction.Privacy)
                settingOptionList[4] -> _settingNavigationEvent.emit(SettingAction.ServiceTerms)
                settingOptionList[5] -> _settingNavigationEvent.emit(SettingAction.Logout)

                userInfoOptionList[5] -> _userInfoNavigationEvent.emit(UserInfoAction.ResetDetailInfo)
                userInfoOptionList[6] -> _userInfoNavigationEvent.emit(UserInfoAction.ResetPassword)
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
            getUserInfoUseCase(userId.value.successOrNull() ?: -1)
                .onSuccess {
                    _userInfo.value = UiState.Success(it.toUiModel())
                    Log.d(TAG, "getUserInfo: ${userInfo.value.successOrNull()}")
                }
                .onError {

                    catchError(it)
                }
            dismissLoading()
        }
    }

    fun withdrawal() {
        baseViewModelScope.launch {
            showLoading()
            try {
                _resultWithdrawal.value =
                    UiState.Success(withdrawalUseCase.invoke(userId.value.successOrNull()!!))
            } catch (e: Exception) {
                _resultWithdrawal.value = UiState.Error(e.cause)
            }
            dismissLoading()

        }
    }

    fun getAcquiredBadgeList() {
        baseViewModelScope.launch {
            showLoading()
            getAcquiredBadgeListUseCase(userId.value.successOrNull()!!)
                .onSuccess {
                    _acquiredBadgeList.value =
                        UiState.Success(it.map { model -> model.toUiModel() })
                }
                .onError {
                    catchError(it)
                }
            dismissLoading()
        }
    }

    fun getRunningRecord(startDate: Long, endDate: Long) {
        baseViewModelScope.launch {
            showLoading()
            getRunningRecordUseCase(userId.value.successOrNull()!!, startDate, endDate)
                .onSuccess {
                    Log.d(TAG, "마이페이지 뷰모델 getRunningRecord: ${it.raceList}")
                    _runningRecord.value = UiState.Success(it)
                    Log.d(TAG, "getRunningRecord: ")
                }
                .onError {
                }
            dismissLoading()
        }
    }

    fun clearRunningRecord() {
        _runningRecord.value = UiState.Loading
    }

    fun logout() {
        baseViewModelScope.launch {
            logoutUseCase.invoke()
        }
    }

    fun getNotificationSettingState() {
        baseViewModelScope.launch {
            showLoading()
            getNotificationSettingStateUseCase(userId.value.successOrNull() ?: -1)
                .onSuccess {
                    _notificationSettingState.value = UiState.Success(it)
                }
                .onError {
                    _notificationSettingState.value = UiState.Error(it.cause)
                }
            dismissLoading()

        }
    }

    fun modifyNotificationState(target: Int, state: Int) {
        baseViewModelScope.launch {
            val response = modifyNotificationSettingStateUseCase(
                userId.value.successOrNull() ?: -1,
                target,
                state
            )
            if (response) Log.d(TAG, "modifyNotificationState: 알림 변경 완료")
        }
    }

    fun getMatchingHistoryList() {
        baseViewModelScope.launch {
            showLoading()
            getMatchingHistoryUseCase(userId.value.successOrNull()!!)
                .onSuccess {
                    _matchingHistoryList.value = UiState.Success(it)
                }
                .onError {
                    Log.d(TAG, "getMatchingHistoryList: ${it.cause}")
                }
            dismissLoading()
        }
    }

    override fun onClick(title: String) {
        setSettingNavigate(title)
    }
}