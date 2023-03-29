package com.d204.rumeet.ui.chatting.chatting_list

import android.util.Log
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.chatting.GetChattingRoomUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
import com.d204.rumeet.ui.chatting.chatting_list.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingListViewModel @Inject constructor(
    private val getChattingRoomUseCase: GetChattingRoomUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : BaseViewModel(), ChattingRoomClickListener {

    private val _chattingListSideEffect: MutableSharedFlow<ChattingListSideEffect> = MutableSharedFlow(replay = 1, extraBufferCapacity = 10)
    val chattingListSideEffect: SharedFlow<ChattingListSideEffect> get() = _chattingListSideEffect.asSharedFlow()

    private val _chattingList: MutableStateFlow<UiState<List<ChattingRoomUiModel>>> = MutableStateFlow(UiState.Loading)
    val chattingList: StateFlow<UiState<List<ChattingRoomUiModel>>> get() = _chattingList.asStateFlow()

    fun requestChattingRoom() {
        baseViewModelScope.launch {
            showLoading()
            val userId = getUserIdUseCase()
            getChattingRoomUseCase(userId)
                .onSuccess { result ->
                    _chattingListSideEffect.emit(
                        ChattingListSideEffect.SuccessGetChattingList(
                            result.isEmpty()
                        )
                    )
                    _chattingList.emit(UiState.Success(result.map { it.toUiModel() }))
                }
                .onError { e ->
                    catchError(e) }
            dismissLoading()
        }
    }

    override fun onChattingRoomClick(roomId: Int) {
        baseViewModelScope.launch {
            _chattingListSideEffect.emit(ChattingListSideEffect.NavigateChattingRoom(roomId))
        }
    }
}