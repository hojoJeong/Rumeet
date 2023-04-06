package com.d204.rumeet.ui.notification

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.running.AcceptRunningRequestUseCase
import com.d204.rumeet.domain.usecase.running.DenyRunningRequestUseCase
import com.d204.rumeet.domain.usecase.user.*
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getFriendRequestListUseCase: GetFriendRequestListUseCase,
    private val getRunningRequestListUseCase: GetRunningRequestListUseCase,
    private val acceptRequestFriendUseCase: AcceptRequestFriendUseCase,
    private val rejectRequestFriendUseCase: RejectRequestFriendUseCase,
    private val acceptRunningRequestUseCase: AcceptRunningRequestUseCase,
    private val denyRunningRequestUseCase: DenyRunningRequestUseCase
) :
    BaseViewModel() {
    private val _notificationAction: MutableSharedFlow<NotificationAction> = MutableSharedFlow()
    val notificationAction: SharedFlow<NotificationAction> get() = _notificationAction.asSharedFlow()

    private val _friendRequestList: MutableStateFlow<UiState<List<NotificationListDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendRequestList: StateFlow<UiState<List<NotificationListDomainModel>>> get() = _friendRequestList.asStateFlow()

    private val _runningRequestList: MutableStateFlow<UiState<List<RunningRequestDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val runningRequestList: StateFlow<UiState<List<RunningRequestDomainModel>>> get() = _runningRequestList.asStateFlow()


    var userId = -1
    fun getNotificationList() {
        baseViewModelScope.launch {
            showLoading()
            userId = getUserIdUseCase()
            getRunningRequestListUseCase(getUserIdUseCase())
                .onSuccess {
                    dismissLoading()

//                    _notificationAction.emit(NotificationAction.RunningRequest(it))
                    _runningRequestList.value = UiState.Success(it)
                    Log.d(TAG, "getNotificationList 러닝 초대: $it")
                }
                .onError {
                }
        }
        baseViewModelScope.launch {
            getFriendRequestListUseCase(getUserIdUseCase())
                .onSuccess {
                    dismissLoading()
                    _friendRequestList.value = UiState.Success(it)
                    Log.d(
                        TAG,
                        "getNotificationList 친구 초대: ${friendRequestList.value.successOrNull()}"
                    )
                }
                .onError {
                    _friendRequestList.value = UiState.Error(it.cause)
                }
            dismissLoading()
        }
    }

    fun acceptRequestFriend(friendId: Int, myId: Int) {
        baseViewModelScope.launch {
            if (acceptRequestFriendUseCase(friendId, myId)) {
                getNotificationList()
            }
        }
    }

    fun denyRequestFriend(friendId: Int, myId: Int) {
        baseViewModelScope.launch {
            if (rejectRequestFriendUseCase(friendId, myId)) {
                getNotificationList()
            }
        }
    }

    fun acceptRequestRunning(raceId: Int, index: Int) {
        baseViewModelScope.launch {
            _notificationAction.emit(NotificationAction.AcceptRunningRequest(raceId, index))
            getNotificationList()
        }
    }

    fun denyRequestRunning(raceId: Int, index: Int) {
        baseViewModelScope.launch {
            if (denyRunningRequestUseCase(raceId)) {
                _notificationAction.emit(NotificationAction.DenyRunningRequest(raceId, index))
                getNotificationList()
            }
        }
    }
}