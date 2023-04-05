package com.d204.rumeet.ui.chatting

import android.util.Log
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.chatting.GetChattingDataUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.chatting.model.ChattingMessageUiModel
import com.d204.rumeet.ui.chatting.model.toUiList
import com.d204.rumeet.util.amqp.ChattingAMQPMananer
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getChattingDataUseCase: GetChattingDataUseCase
) : BaseViewModel() {

    private val _chattingSideEffect: MutableSharedFlow<ChattingSideEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 10)
    val chattingSideEffect: SharedFlow<ChattingSideEffect> get() = _chattingSideEffect.asSharedFlow()

    private val _chattingDataList: MutableStateFlow<UiState<List<ChattingMessageUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val chattingDataList: StateFlow<UiState<List<ChattingMessageUiModel>>> get() = _chattingDataList.asStateFlow()

    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private val _chattingUserId : MutableStateFlow<Int> = MutableStateFlow(-1)
    val chattingUserId : StateFlow<Int> get() = _chattingUserId.asStateFlow()

    private var repeatDelete = 0

    fun requestChattingData(roomId: Int, otherUserId : Int, noReadCnt : Int) {
        baseViewModelScope.launch {
            getChattingDataUseCase(roomId)
                .onSuccess {
                    _userId.emit(getUserIdUseCase())
                    _chattingUserId.emit(otherUserId)
                    _chattingSideEffect.emit(ChattingSideEffect.SuccessChattingData(userId = userId.value))
                    _chattingDataList.emit(UiState.Success(it.chat.toUiList()))

                    repeatDelete = noReadCnt
                    ChattingAMQPMananer.chattingQueueName = "chat.queue.${roomId}.${userId.value}"
                    startSubscribe()

                }
                .onError { e -> catchError(e) }
        }
    }

    private fun startSubscribe() {
        ChattingAMQPMananer.setReceiveMessage(object : DefaultConsumer(ChattingAMQPMananer.chattingChanel) {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                if(repeatDelete != 0){
                    repeatDelete--
                } else{
                    try {
                        val message = Gson().fromJson(String(body), ChattingMessageModel::class.java)
                        val check = _chattingSideEffect.tryEmit(ChattingSideEffect.ReceiveChatting(message))
                        Log.d("chatting", "handleDelivery: $check")
                    }catch (e : Exception){
                        Log.e("chatting", "handleDelivery: ${e.message}")
                    }
                }
            }
        })
    }

    fun sendChatting() {
        baseViewModelScope.launch {
            _chattingSideEffect.emit(ChattingSideEffect.SendChatting)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ChattingAMQPMananer.unSubscribeChatting()
    }
}