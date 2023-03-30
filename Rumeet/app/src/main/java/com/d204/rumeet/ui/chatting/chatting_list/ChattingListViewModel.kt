package com.d204.rumeet.ui.chatting.chatting_list

import android.util.Log
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.chatting.GetChattingRoomUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.chatting.ChattingSideEffect
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
import com.d204.rumeet.ui.chatting.chatting_list.model.toUiModel
import com.d204.rumeet.util.AMQPManager
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingListViewModel @Inject constructor(
    private val getChattingRoomUseCase: GetChattingRoomUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : BaseViewModel(), ChattingRoomClickListener {

    private val _chattingListSideEffect: MutableSharedFlow<ChattingListSideEffect> =
        MutableSharedFlow()
    val chattingListSideEffect: SharedFlow<ChattingListSideEffect> get() = _chattingListSideEffect.asSharedFlow()

    private val _chattingList: MutableStateFlow<UiState<List<ChattingRoomUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val chattingList: StateFlow<UiState<List<ChattingRoomUiModel>>> get() = _chattingList.asStateFlow()

    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private var flag = false

    fun requestChattingRoom() {
        baseViewModelScope.launch {
            showLoading()
            _userId.emit(getUserIdUseCase())
            getChattingRoomUseCase(userId.value)
                .onSuccess { result ->
                    AMQPManager.userQueueName = "user.queue.${userId.value}"
                    startChattingListSubscribe()
                    _chattingListSideEffect.emit(
                        ChattingListSideEffect.SuccessGetChattingList(
                            result.isEmpty()
                        )
                    )
                    _chattingList.emit(UiState.Success(result.map { it.toUiModel() }))
                }
                .onError { e ->
                    catchError(e)
                }
            dismissLoading()
        }
    }

    override fun onChattingRoomClick(roomId: Int, profile: String, otherUserId: Int, noReadCnt : Int) {
        baseViewModelScope.launch {
            _chattingListSideEffect.emit(
                ChattingListSideEffect.NavigateChattingRoom(
                    roomId,
                    profile,
                    otherUserId,
                    noReadCnt
                )
            )
        }
    }

    private fun startChattingListSubscribe() {
        AMQPManager.setChattingListReceive(object : DefaultConsumer(AMQPManager.userChannel) {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                try {
                    if (flag) {
                        val message =
                            Gson().fromJson(String(body), Array<ChattingRoomUiModel>::class.java)
                                .toList()
                        CoroutineScope(Dispatchers.IO).launch {
                            _chattingListSideEffect.tryEmit(
                                ChattingListSideEffect.SuccessNewChattingList(
                                    message
                                )
                            )
                        }
                    } else {
                        flag = true
                    }

                } catch (e: Exception) {
                    Log.e("chattinglist", "handleDelivery: ${e.message}")
                }
            }
        })
    }
}