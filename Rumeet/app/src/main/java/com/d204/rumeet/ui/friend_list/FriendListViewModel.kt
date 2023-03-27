package com.d204.rumeet.ui.friend_list

import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.friend.GetFriendInfoUseCase
import com.d204.rumeet.domain.usecase.friend.GetFriendListUseCase
import com.d204.rumeet.domain.usecase.user.SearchUsersUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.friend_list.model.FriendInfoModel
import com.d204.rumeet.ui.friend_list.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val getFriendInfoUseCase: GetFriendInfoUseCase,
    private val getFriendListUseCase: GetFriendListUseCase,

) : BaseViewModel(), FriendListClickListener {
    private val _friendListAction: MutableSharedFlow<FriendListAction> = MutableSharedFlow()
    val friendListAction: SharedFlow<FriendListAction> get() = _friendListAction.asSharedFlow()

    private val _friendList: MutableStateFlow<UiState<List<FriendInfoModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendList: StateFlow<UiState<List<FriendInfoModel>>> get() = _friendList.asStateFlow()

    fun requestFriendList() {
        baseViewModelScope.launch {
            showLoading()
            getFriendListUseCase()
                .onSuccess { response ->
                    _friendList.value = UiState.Success(response.map { it.toUiModel() })
                }
                .onError { e -> catchError(e) }
            dismissLoading()
        }
    }

    fun getFriendInfo(userId: Int) {
        baseViewModelScope.launch {
            getFriendInfoUseCase(userId)
                .onSuccess { _friendListAction.emit(FriendListAction.ShowFriendInfoDialog(it.toUiModel())) }
                .onError { e -> catchError(e) }
        }
    }

    fun sortRecentlyRunFriend(){
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SortRecentlyRunFriend)
        }
    }

    fun sortRunTogetherFriend(){
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SortRunTogetherFriend)
        }
    }

    override fun onFriendListClick(userId: Int) {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SearchFriend(userId))
        }
    }

}