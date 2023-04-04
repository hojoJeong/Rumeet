package com.d204.rumeet.ui.running

import android.util.Log
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.repository.RunningRepository
import com.d204.rumeet.domain.usecase.running.RecordRunningUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.domain.usecase.user.GetUserInfoUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.option.model.*
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val recordRunningUseCase: RecordRunningUseCase,
    private val runningRepository: RunningRepository
) : BaseViewModel() {

    private val _runningSideEffect: MutableSharedFlow<RunningSideEffect> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 100)
    val runningSideEffect: SharedFlow<RunningSideEffect> get() = _runningSideEffect.asSharedFlow()

    private val _runningRecordState : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val runningRecordState : StateFlow<Boolean> get() = _runningRecordState.asStateFlow()

    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    val runningTypeModel = RunningTypeModel()

    fun startRun(userId: Int, roomId: Int) {
        baseViewModelScope.launch {
            startRunningSubscribe(userId, roomId)
        }
    }

    // 솔로 시작 (고스트 타입:0)
    fun startSoloGame(gameType: Int) {
        baseViewModelScope.launch {
            _userId.emit(getUserIdUseCase()) // userId 가져오기
            runningRepository.startSolo(userId.value, gameType, 0)
                .onSuccess { response ->
                    _runningSideEffect.emit(RunningSideEffect.SuccessSoloData(response))
                    Log.d("러밋_RunningViewModel", "startGetGhost: 고스트 API 요청 결과: $response")
                }
                .onError { e ->
                    catchError(e)
                    Log.d("러밋_RunningViewModel", "startGetGhost: Error 발생 Error 발생 !!!!!! ${e.message} ")
                }
        }
    }

    // 레이스 종료후 보내기
    fun raceRecord(
        userId: Int,
        raceId: Int,
        mode: Int,
        velocity: Float,
        time: Int,
        heartRate: Int,
        success: Int,
        polyline: File?
    ) {
        baseViewModelScope.launch {
            recordRunningUseCase(userId, raceId, mode, velocity, time, heartRate, success, polyline)
                .onSuccess { _runningRecordState.emit(true) }
                .onError { e -> catchError(e) }
        }
    }

    fun getUserInfo(userId: Int) {
        baseViewModelScope.launch {
            try {
                getUserInfoUseCase(userId)
                    .onSuccess { _runningSideEffect.emit(RunningSideEffect.SuccessUserInfo(it)) }
                    .onError { e -> catchError(e) }
            } catch (e: Exception) {
                Log.e("TAG", "getUserInfo: catch ${e.message}")
            }
        }
    }

    fun getPartnerInfo(partnerId: Int) {
        baseViewModelScope.launch {
            try {
                getUserInfoUseCase(partnerId)
                    .onSuccess { _runningSideEffect.emit(RunningSideEffect.SuccessPartnerInfo(it)) }
                    .onError { e -> catchError(e) }
            } catch (e: Exception) {
                Log.e("TAG", "getUserInfo: catch ${e.message}")
            }
        }
    }

    private fun startRunningSubscribe(userId: Int, roomId: Int) {
        RunningAMQPManager.receiveRunning(
            roomId,
            userId,
            object : DefaultConsumer(RunningAMQPManager.runningChannel) {
                override fun handleDelivery(
                    consumerTag: String?,
                    envelope: Envelope?,
                    properties: AMQP.BasicProperties?,
                    body: ByteArray
                ) {
                    val distance = String(body)
                    Log.d("get partner running", "handleDelivery: ${distance}")
                    _runningSideEffect.tryEmit(RunningSideEffect.SuccessRunning(distance.toInt()))
                }
            })
    }

    // state = 1 싱글, state = 2 멀티
    fun setGameType(state: RunningType) {
        runningTypeModel.runningType = state
    }

    // 거리 or 협동의 난이도
    fun setDistance(distance: RunningDistance) {
        runningTypeModel.distance = distance
    }

    // 누구와 달릴지
    fun setRunningDetailType(who: RunningDetailType) {
        runningTypeModel.runningDetailType = who
    }

    // 난이도
    fun setRunningDifficulty(difficulty: RunningDifficulty) {
        runningTypeModel.runningDifficulty = difficulty
    }
}