package com.d204.rumeet.ui.chatting

import android.util.Log
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.chatting.GetChattingDataUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.util.AMQPManager
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getChattingDataUseCase: GetChattingDataUseCase
) : BaseViewModel() {

    private val _chattingSideEffect: MutableSharedFlow<ChattingSideEffect> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 10)
    val chattingSideEffect: SharedFlow<ChattingSideEffect> get() = _chattingSideEffect.asSharedFlow()

    private val _chattingDataList: MutableStateFlow<UiState<List<ChattingMessageModel>>> =
        MutableStateFlow(UiState.Loading)
    val chattingDataList: StateFlow<UiState<List<ChattingMessageModel>>> get() = _chattingDataList.asStateFlow()

    fun requestChattingData(roomId: Int) {
        baseViewModelScope.launch {
            getChattingDataUseCase(roomId)
                .onSuccess {
                    val userId = getUserIdUseCase()
                    AMQPManager.queueName = "chat.queue.${roomId}.${2}"

                    _chattingSideEffect.emit(ChattingSideEffect.SuccessChattingData(userId = userId))
                    _chattingDataList.emit(UiState.Success(it.chat))
                    startSubscribe()
                }
                .onError { e -> catchError(e) }
        }
    }

    private fun startSubscribe() {
        AMQPManager.setReceiveMessage(object : DefaultConsumer(AMQPManager.channel) {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                Log.d("TAG", "handleDelivery: ${String(body)}")
                val message = Gson().fromJson(String(body), ChattingMessageModel::class.java)
                _chattingSideEffect.tryEmit(ChattingSideEffect.ReceiveChatting(message))
            }
        })
    }

    fun sendChatting() {
        baseViewModelScope.launch {
            _chattingSideEffect.emit(ChattingSideEffect.SendChatting)
        }
    }
}