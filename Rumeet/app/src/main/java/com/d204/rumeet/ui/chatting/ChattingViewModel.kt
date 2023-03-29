package com.d204.rumeet.ui.chatting

import androidx.lifecycle.ViewModel
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.model.chatting.ChattingModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.chatting.GetChattingDataUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val getChattingDataUseCase: GetChattingDataUseCase
): BaseViewModel() {

    private val _chattingSideEffect : MutableSharedFlow<ChattingSideEffect> = MutableSharedFlow()
    val chattingSideEffect : SharedFlow<ChattingSideEffect> get() = _chattingSideEffect.asSharedFlow()

    private val _chattingDataList : MutableStateFlow<UiState<List<ChattingMessageModel>>> = MutableStateFlow(UiState.Loading)
    val chattingDataList : StateFlow<UiState<List<ChattingMessageModel>>> get() = _chattingDataList.asStateFlow()

    fun requestChattingData(roomId : Int){
        baseViewModelScope.launch {
            getChattingDataUseCase(roomId)
                .onSuccess { _chattingDataList.emit(UiState.Success(it.chat)) }
                .onError { e -> catchError(e) }
        }
    }
}