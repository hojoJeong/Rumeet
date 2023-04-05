package com.d204.rumeet.ui.running.matching

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.CountDownTimer
import android.util.Log
import com.d204.rumeet.domain.model.user.RunningSoloDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.repository.RunningRepository
import com.d204.rumeet.domain.successOr
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
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
    private val getUserIdUseCase: GetUserIdUseCase,
    private val runningRepository: RunningRepository
) : BaseViewModel() {
    private val _runningMatchingSideEffect: MutableSharedFlow<RunningMatchingSideEffect> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 10)
    val runningMatchingSideEffect: SharedFlow<RunningMatchingSideEffect> get() = _runningMatchingSideEffect.asSharedFlow()

    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private val _gameType: MutableStateFlow<Int> = MutableStateFlow(-1)
    val gameType: StateFlow<Int> get() = _gameType.asStateFlow()

    private val _ghostType: MutableStateFlow<Int> = MutableStateFlow(-1)
    val ghostType: StateFlow<Int> get() = _ghostType.asStateFlow()

    private val _matchingResult: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val matchingState: StateFlow<Boolean> get() = _matchingResult.asStateFlow()

    private val _otherPlayerId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val otherPlayer: StateFlow<Int> get() = _otherPlayerId.asStateFlow()

    private val _gameStartInfo: MutableStateFlow<UiState<RunningSoloDomainModel>> = MutableStateFlow(UiState.Loading)
    val gameStartInfo : StateFlow<UiState<RunningSoloDomainModel>> get() = _gameStartInfo.asStateFlow()

    private lateinit var timer: CountDownTimer

    fun startMatching(gameType: Int) {
        baseViewModelScope.launch {
            _userId.emit(getUserIdUseCase())
            _gameType.emit(gameType)

            Log.d(TAG, "startMatching: ${userId.value}")
            val startModel = RunningMatchingRequestModel(userId.value, gameType)
            RunningAMQPManager.startMatching(jsonToString(startModel) ?: throw Exception("NO TYPE"))

            startMatchingSubscribe()
            startTimer()
        }
    }

    fun startGhost(gameType: Int, ghostType: Int) {
        baseViewModelScope.launch {
            _userId.emit(getUserIdUseCase())
            _gameType.emit(gameType)
            _ghostType.emit(ghostType)

            startTimer()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick: ${millisUntilFinished}")
            }

            override fun onFinish() {
                val startModel = RunningMatchingRequestModel(userId.value, gameType.value)
                RunningAMQPManager.failMatching(jsonToString(startModel) ?: throw Exception("NO TYPE"))
                _matchingResult.tryEmit(false)
                val response =
                    _runningMatchingSideEffect.tryEmit(RunningMatchingSideEffect.FailMatching)
                Log.d(TAG, "onFinish: $response")
            }
        }.start()
    }

    fun startGetGhost() {
        baseViewModelScope.launch {
            Log.d(TAG, "startGetGhost: StartSolo API 통신 시작!! gameType:${gameType.value}, ghostType:${ghostType.value}")
            runningRepository.startSolo(userId.value, gameType.value, ghostType.value)
                .onSuccess { response ->
                    _runningMatchingSideEffect.emit(RunningMatchingSideEffect.SuccessGhostData(response))
                    Log.d(TAG, "startGetGhost: 고스트 API 요청 결과: $response")
                    timer.cancel()
                    timer.onFinish()

                }
                .onError { e ->
                    catchError(e)
                    Log.d(TAG, "startGetGhost: Error 발생 Error 발생 !!!!!! ${e.message} ")
                }
        }
    }

    private fun startMatchingSubscribe() {
        RunningAMQPManager.subscribeMatching(userId.value, object :
            DefaultConsumer(RunningAMQPManager.runningChannel) {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                try {
                    timer.cancel()
                    val response = Gson().fromJson(String(body), RunningRaceModel::class.java)
                    if (response.userId != userId.value) {
                        _otherPlayerId.tryEmit(response.userId)
                    } else{
                        _otherPlayerId.tryEmit(response.partnerId)
                    }
                    _matchingResult.tryEmit(true)
                    val test = _runningMatchingSideEffect.tryEmit(RunningMatchingSideEffect.SuccessMatching(_userId.value, response.id, _otherPlayerId.value))
                    Log.d(TAG, "handleDelivery: $test")
                } catch (e: Exception) {
                    Log.e(TAG, "handleDelivery: ${e.message}")
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        if(!_matchingResult.value){
            val startModel = RunningMatchingRequestModel(userId.value, gameType.value)
            RunningAMQPManager.failMatching(jsonToString(startModel) ?: throw Exception("NO TYPE"))
            Log.d(TAG, "onCleared: cancel matching")
        }

    }
}

private const val TAG = "러밋_RunningMatchingViewModel"