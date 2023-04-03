package com.d204.rumeet.ui.friend.list

import android.content.ContentValues.TAG
import android.provider.Contacts.Intents.UI
import android.util.Log
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val getFriendInfoUseCase: GetFriendInfoUseCase,
    private val getFriendListUseCase: GetFriendListUseCase,
    private val searchFriendUseCase: SearchFriendUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getFriendDetailInfoUseCase: GetFriendDetailInfoUseCase

) : BaseViewModel(), FriendListClickListener {
    private val _friendListAction: MutableSharedFlow<FriendListAction> = MutableSharedFlow()
    val friendListAction: SharedFlow<FriendListAction> get() = _friendListAction.asSharedFlow()

    private val _friendList: MutableStateFlow<UiState<List<FriendListDomainModel>>> =
        MutableStateFlow(UiState.Loading)
    val friendList: StateFlow<UiState<List<FriendListDomainModel>>> get() = _friendList.asStateFlow()


    fun requestFriendList(type: Int) {
        baseViewModelScope.launch {
            showLoading()
            getFriendListUseCase(type)
                .onSuccess { response ->
                    _friendListAction.emit(FriendListAction.SuccessFriendList(response.size))
                    _friendList.value = UiState.Success(response)
                }
                .onError { e -> catchError(e) }
            dismissLoading()
        }
    }

    fun searchFriendList(searchNickname : String){
        baseViewModelScope.launch {
            showLoading()
            searchFriendUseCase(getUserIdUseCase(),  searchNickname)
                .onSuccess { response ->
                    Log.d(TAG, "searchFriendList: $response")
                    _friendListAction.emit(FriendListAction.SuccessSearchFriend)
                    _friendList.value = UiState.Success(response)
                }
                .onError { e -> catchError(e) }
            dismissLoading()
        }
    }

    fun getFriendInfo(userId: Int) {
        baseViewModelScope.launch {
            showLoading()
            getFriendDetailInfoUseCase(userId)
                .onSuccess { _friendListAction.emit(FriendListAction.SuccessFriendInfo(it)) }
                .onError { e -> catchError(e) }
            dismissLoading()
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

    fun navigateAddFriend(){
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.NavigateAddFriend)
        }
    }

    override fun onFriendListClick(userId: Int) {
        baseViewModelScope.launch {
            _friendListAction.emit(FriendListAction.SearchFriend(userId))
        }
    }

}