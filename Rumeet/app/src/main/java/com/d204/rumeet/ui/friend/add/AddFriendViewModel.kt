package com.d204.rumeet.ui.friend.add

import com.d204.rumeet.data.remote.dto.AlreadyFriendException
import com.d204.rumeet.data.remote.dto.AlreadyRequestFriendException
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.friend.RequestFriendUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.domain.usecase.user.SearchUsersUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.add.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val requestFriendUseCase: RequestFriendUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : BaseViewModel(), AddFriendListClickListener {
    private val _addFriendAction: MutableSharedFlow<AddFriendAction> = MutableSharedFlow()
    val addFriendAction: SharedFlow<AddFriendAction> = _addFriendAction.asSharedFlow()

    private val _searchUserListState: MutableStateFlow<UiState<List<UserListUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val searchUserListState: StateFlow<UiState<List<UserListUiModel>>> get() = _searchUserListState.asStateFlow()

    fun requestSearchFriend(nickname: String) {
        baseViewModelScope.launch {
            showLoading()
            searchUsersUseCase(nickname)
                .onSuccess { result ->
                    _addFriendAction.emit(AddFriendAction.SuccessRequestFriendList(result.size))
                    _searchUserListState.emit(UiState.Success(result.map { it.toUiModel() }))
                }
                .onError { e -> catchError(e) }
            dismissLoading()
        }
    }

    fun requestFriend(fromUserId : Int) {
        baseViewModelScope.launch {
            showLoading()
            val userId = getUserIdUseCase()
            requestFriendUseCase(userId, fromUserId)
                .onSuccess { _addFriendAction.emit(AddFriendAction.SuccessRequestFriend) }
                .onError { e ->
                    when(e){
                        is AlreadyRequestFriendException -> {
                            _addFriendAction.emit(AddFriendAction.AlreadyRequestFriend)
                        }
                        is AlreadyFriendException -> {
                            _addFriendAction.emit(AddFriendAction.AlreadyFriend)
                        }
                        else -> {catchError(e)}
                    }
                }
            dismissLoading()
        }
    }

    override fun searchUserClick(data: UserListUiModel) {
        baseViewModelScope.launch {
            _addFriendAction.emit(AddFriendAction.ShowUserInfoDialog(data))
        }
    }
}