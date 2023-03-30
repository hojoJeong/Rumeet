package com.d204.rumeet.ui.running.matching

import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningMatchingBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
import com.d204.rumeet.ui.find_account.FindAccountAction
import com.d204.rumeet.ui.running.matching.model.RunningMatchingRequestModel
import com.d204.rumeet.ui.running.matching.model.RunningRaceModel
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunningMatchingFragment :
    BaseFragment<FragmentRunningMatchingBinding, RunningMatchingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_matching

    override val viewModel: RunningMatchingViewModel by viewModels()
    private var matchingState = false


    override fun initStartView() {
        viewModel.startMatching(3)
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.runningMatchingSideEffect.collectLatest {
                when (it) {
                    is RunningMatchingSideEffect.FailMatching -> {
                        // 매칭 실패,  프래그먼트 이동 후 러닝옵션으로 pop
                    }
                    is RunningMatchingSideEffect.SuccessMatching -> {
                        // 매칭 성공, 달리기 3초 후 시작
                    }
                }
            }
        }
    }


    override fun initAfterBinding() {
        CoroutineScope(Dispatchers.IO).launch {
            RunningAMQPManager.startMatching(
                Gson().toJson(RunningMatchingRequestModel(27, 5))
            )
            test()
        }

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (matchingState) cancel()
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish: fail")
                RunningAMQPManager.failMatching(
                    Gson().toJson(RunningMatchingRequestModel(27, 5))
                )
            }
        }.start()
    }

    private fun test() {
        CoroutineScope(Dispatchers.IO).launch {
            RunningAMQPManager.subscribeMatching(object :
                DefaultConsumer(RunningAMQPManager.runningChannel) {
                override fun handleDelivery(
                    consumerTag: String?,
                    envelope: Envelope?,
                    properties: AMQP.BasicProperties?,
                    body: ByteArray
                ) {
                    // 매칭 완료, 3 2 1 후 게임 시작 -> 2
                    try {
                        val response = Gson().fromJson(String(body), RunningRaceModel::class.java)
//                        matchingState = true
                        Log.d(TAG, "handleDelivery: $response")
                    } catch (e: Exception) {
                        Log.e(TAG, "handleDelivery: ${e.message}")
                    }
                }
            })
        }
    }
}

private const val TAG = "RunningMatchingFragment"