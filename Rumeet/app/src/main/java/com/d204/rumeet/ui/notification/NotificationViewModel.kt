package com.d204.rumeet.ui.notification

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.*
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getFriendRequestListUseCase: GetFriendRequestListUseCase,
    private val getRunningRequestListUseCase: GetRunningRequestListUseCase,
    private val acceptRequestFriendUseCase: AcceptRequestFriendUseCase,
    private val rejectRequestFriendUseCase: RejectRequestFriendUseCase
) :
    BaseViewModel() {
    private val _friendRequestList: MutableStateFlow<UiState<List<NotificationListDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendRequestList: StateFlow<UiState<List<NotificationListDomainModel>>> get() = _friendRequestList.asStateFlow()

    private val _runningRequestList: MutableStateFlow<UiState<List<RunningRequestDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val runningRequestList: StateFlow<UiState<List<RunningRequestDomainModel>>> get() = _runningRequestList

    private val _resultReplyRequest: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Loading)
    val resultReplyRequest: StateFlow<UiState<Boolean>> get() = _resultReplyRequest.asStateFlow()

    fun getNotificationList() {
        baseViewModelScope.launch {
            showLoading()
            getRunningRequestListUseCase(getUserIdUseCase())
                .onSuccess {
                    dismissLoading()
                    _runningRequestList.value = UiState.Success(it)
                    Log.d(TAG, "getNotificationList 러닝 초대: $it")
                }
                .onError { dismissLoading() }

            getFriendRequestListUseCase(getUserIdUseCase())
                .onSuccess {
                    dismissLoading()
                    _friendRequestList.value = UiState.Success(it)
                    Log.d(TAG, "getNotificationList: $it")
                }
                .onError {
                    dismissLoading()
                    _friendRequestList.value = UiState.Error(it.cause)
                }
        }
    }

    fun acceptRequestFriend(friendId: Int, myId: Int){
        baseViewModelScope.launch {
            if(acceptRequestFriendUseCase(friendId,myId)){
                _resultReplyRequest.value = UiState.Success(true)
            } else {

            }
        }
    }

    fun rejectRequestFriend(friendId: Int, myId: Int){
        baseViewModelScope.launch {
            if(rejectRequestFriendUseCase(friendId, myId)){
                _resultReplyRequest.value = UiState.Success(true)
            } else {

            }
        }
    }
}