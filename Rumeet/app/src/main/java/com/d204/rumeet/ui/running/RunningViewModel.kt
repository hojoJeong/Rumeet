package com.d204.rumeet.ui.running

import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.running.option.model.*
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RunningViewModel : BaseViewModel() {

    private val _runningSideEffect : MutableSharedFlow<RunningSideEffect> = MutableSharedFlow(replay = 1, extraBufferCapacity = 100)
    val runningSideEffect : SharedFlow<RunningSideEffect> get() = _runningSideEffect.asSharedFlow()

    val runningTypeModel = RunningTypeModel()

    fun startRun(userId : Int, roomId : Int){
        baseViewModelScope.launch {
            startRunningSubscribe(userId,roomId)
        }
    }

    private fun startRunningSubscribe(userId : Int, roomId: Int){
        RunningAMQPManager.receiveRunning(roomId, userId, object : DefaultConsumer(RunningAMQPManager.runningChannel){
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                val distance = String(body)
                _runningSideEffect.tryEmit(RunningSideEffect.SuccessRunning(distance.toInt()))
            }
        })
    }

    // state = 1 싱글, state = 2 멀티
    fun setGameType(state: RunningType) {
        runningTypeModel.runningType = state
    }

    // 거리 or 협동의 난이도
    fun setDistance(distance : RunningDistance){
        runningTypeModel.distance = distance
    }

    // 누구와 달릴지
    fun setRunningDetailType(who : RunningDetailType){
        runningTypeModel.runningDetailType = who
    }

    // 난이도
    fun setRunningDifficulty(difficulty: RunningDifficulty){
        runningTypeModel.runningDifficulty = difficulty
    }
}