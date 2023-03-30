package com.d204.rumeet.ui.running.matching

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.CountDownTimer
import android.util.Log
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.matching.model.RunningMatchingRequestModel
import com.d204.rumeet.ui.running.matching.model.RunningRaceModel
import com.d204.rumeet.util.amqp.ChattingAMQPMananer
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.d204.rumeet.util.jsonToString
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningMatchingViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase
) : BaseViewModel() {
    private val _runningMatchingSideEffect : MutableSharedFlow<RunningMatchingSideEffect> = MutableSharedFlow()
    val runningMatchingSideEffect : SharedFlow<RunningMatchingSideEffect> get() = _runningMatchingSideEffect.asSharedFlow()

    private val _userId : MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId : StateFlow<Int> get() = _userId.asStateFlow()

    private val _gameType : MutableStateFlow<Int> = MutableStateFlow(-1)
    val gameType : StateFlow<Int> get() = _gameType.asStateFlow()

    private val _matchingResult : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val matchingState : StateFlow<Boolean> get() = _matchingResult.asStateFlow()

    private val _otherPlayerId : MutableStateFlow<Int> = MutableStateFlow(-1)
    val otherPlayer : StateFlow<Int> get() = _otherPlayerId.asStateFlow()

    fun startMatching(gameType : Int){
        baseViewModelScope.launch {
//            _userId.emit(getUserIdUseCase())
//            _gameType.emit(gameType)
//
//            val startModel = RunningMatchingRequestModel(userId.value, gameType)
//            RunningAMQPManager.startMatching(jsonToString(startModel) ?: throw Exception("NO TYPE"))
//
//            startMatchingSubscribe()
//            startTimer()
        }
    }

    private fun startTimer(){
        object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long){}
            override fun onFinish() {
                val startModel = RunningMatchingRequestModel(userId.value, gameType.value)
                RunningAMQPManager.failMatching(jsonToString(startModel) ?: throw Exception("NO TYPE"))

                _matchingResult.tryEmit(false)
                _runningMatchingSideEffect.tryEmit(RunningMatchingSideEffect.FailMatching)
            }
        }
    }

    private fun startMatchingSubscribe(){
        RunningAMQPManager.subscribeMatching(object :
            DefaultConsumer(RunningAMQPManager.runningChannel) {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                // 매칭 완료, 3 2 1 후 게임 시작 -> 데이터 2개 넘어옴, 상대와 내 것
                try {
                    val response = Gson().fromJson(String(body), RunningRaceModel::class.java)
                    if(response.id != userId.value){
                        _otherPlayerId.tryEmit(response.id)
                    }

                    val test = _matchingResult.tryEmit(true)
                    _runningMatchingSideEffect.tryEmit(RunningMatchingSideEffect.SuccessMatching)

                    Log.d("runningviewmodel", "handleDelivery: $test")
                }catch (e : Exception){
                    Log.e("runningviewmodel", "handleDelivery: ${e.message}", )
                }
            }
        })
    }
}