package com.d204.rumeet.ui.notification

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.GetFriendRequestListUseCase
import com.d204.rumeet.domain.usecase.user.GetRunningRequestListUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getFriendRequestListUseCase: GetFriendRequestListUseCase,
    private val getRunningRequestListUseCase: GetRunningRequestListUseCase
) :
    BaseViewModel() {
    private val _friendRequestList: MutableStateFlow<UiState<List<NotificationListDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendRequestList: StateFlow<UiState<List<NotificationListDomainModel>>> get() = _friendRequestList.asStateFlow()

    private val _runningRequestList: MutableStateFlow<UiState<List<RunningRequestDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val runningRequestList: StateFlow<UiState<List<RunningRequestDomainModel>>> get() = _runningRequestList


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
}