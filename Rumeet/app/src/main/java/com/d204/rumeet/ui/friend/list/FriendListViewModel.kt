package com.d204.rumeet.ui.friend.list

import android.content.ContentValues.TAG
import android.provider.Contacts.Intents.UI
import android.util.Log
import com.d204.rumeet.domain.model.chatting.ChattingCreateModel
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.chatting.CreateChattingRoomUseCase
import com.d204.rumeet.domain.usecase.friend.GetFriendInfoUseCase
import com.d204.rumeet.domain.usecase.friend.GetFriendListUseCase
import com.d204.rumeet.domain.usecase.friend.SearchFriendUseCase
import com.d204.rumeet.domain.usecase.user.GetFriendDetailInfoUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel
import com.d204.rumeet.ui.friend.list.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val getFriendInfoUseCase: GetFriendInfoUseCase,
    private val getFriendListUseCase: GetFriendListUseCase,
    private val searchFriendUseCase: SearchFriendUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getFriendDetailInfoUseCase: GetFriendDetailInfoUseCase,
    private val createChattingRoomUseCase: CreateChattingRoomUseCase

) : BaseViewModel(), FriendListClickListener {
    private val _friendListAction: MutableSharedFlow<FriendListAction> = MutableSharedFlow(replay = 0)
    val friendListAction: SharedFlow<FriendListAction> get() = _friendListAction.asSharedFlow()

    private val _friendList: MutableStateFlow<UiState<List<FriendListDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendList: StateFlow<UiState<List<FriendListDomainModel>>> get() = _friendList.asStateFlow()

    private val _chattingRoom: MutableStateFlow<UiState<ChattingCreateModel>> =
        MutableStateFlow(UiState.Loading)
    val chattingRoom: StateFlow<UiState<ChattingCreateModel>> get() = _chattingRoom.asStateFlow()

    fun requestFriendList(type: Int) {
        baseViewModelScope.launch {
            getFriendListUseCase(type)
                .onSuccess { response ->
                    _friendListAction.emit(FriendListAction.SuccessFriendList(response.size))
                    _friendList.value = UiState.Success(response)
                }
                .onError { e -> catchError(e) }
        }
    }

    fun searchFriendList(searchNickname: String) {
        baseViewModelScope.launch {
            searchFriendUseCase(getUserIdUseCase(), searchNickname)
                .onSuccess { response ->
                    Log.d(TAG, "searchFriendList: $response")
                    _friendListAction.emit(FriendListAction.SuccessSearchFriend)
                    _friendList.value = UiState.Success(response)
                }
                .onError { e -> catchError(e) }
        }
    }

    fun getFriendInfo(userId: Int) {
        baseViewModelScope.launch {
            getFriendDetailInfoUseCase(userId)
                .onSuccess { _friendListAction.emit(FriendListAction.SuccessFriendInfo(it)) }
                .onError { e -> catchError(e) }
        }
    }

    fun sortRecentlyRunFriend() {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SortRecentlyRunFriend)
        }
    }

    fun sortRunTogetherFriend() {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SortRunTogetherFriend)
        }
    }

    fun navigateAddFriend() {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.NavigateAddFriend)
        }
    }

    override fun onFriendListClick(userId: Int) {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SearchFriend(userId))
        }
    }

    fun startRunningOption(friendId: Int) {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.StartRunningOption(friendId))
        }
    }

    fun createChatting(friendId: Int) {
        baseViewModelScope.launch {
            createChattingRoomUseCase(getUserIdUseCase(), friendId)
                .onSuccess {
                    _friendListAction.emit(FriendListAction.CreateChatting(friendId, it))
                    Log.d(TAG, "createChatting: $it")
                }
                .onError {
                    catchError(it)
                }
        }
    }

}