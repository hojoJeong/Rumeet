package com.d204.rumeet.ui.notification

import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.GetFriendRequestListUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val getFriendRequestListUseCase: GetFriendRequestListUseCase) :
    BaseViewModel() {
    private val _friendRequestList: MutableStateFlow<UiState<List<NotificationListDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendRequestList: StateFlow<UiState<List<NotificationListDomainModel>>> get() = _friendRequestList.asStateFlow()

    private val _runningRequestList: MutableStateFlow<UiState<List<NotificationListDomainModel>>> = MutableStateFlow(UiState.Loading)
    val runningRequestList: StateFlow<UiState<List<NotificationListDomainModel>>> get() = _runningRequestList


    fun getNotificationList(){
        baseViewModelScope.launch {
            showLoading()
            getFriendRequestListUseCase()
                .onSuccess {
                    dismissLoading()
                    _friendRequestList.value = UiState.Success(it)
                }
                .onError {
                    dismissLoading()
                    _friendRequestList.value = UiState.Error(it.cause)
                }
        }
    }
}