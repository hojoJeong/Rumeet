package com.d204.rumeet.ui.running.matching

import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningMatchingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.successOrNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RunningMatchingFragment :
    BaseFragment<FragmentRunningMatchingBinding, RunningMatchingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_matching

    override val viewModel: RunningMatchingViewModel by viewModels()
    private val args by navArgs<RunningMatchingFragmentArgs>()

    override fun initStartView() {
        binding.lifecycleOwner = viewLifecycleOwner
        if (args.ghostType > -1) { // 고스트 모드 분기 (내 고스트:1, 랜덤 고스트 : 2)
            Log.d(TAG, "initStartView: 고스트 모드임!! (1:나, 2:랜덤) : ${args.ghostType}")
            viewModel.startGhost(args.gameType, args.ghostType)
        } else { // 매칭인 경우
            viewModel.startMatching(args.gameType)
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.runningMatchingSideEffect.collectLatest {
                    when (it) {
                        is RunningMatchingSideEffect.FailMatching -> {
                            // 매칭 실패,  프래그먼트 이동 후 러닝옵션으로 pop
                            navigate(RunningMatchingFragmentDirections.actionRunningMatchingFragmentToRunningMatchingFailFragment())
                        }
                        is RunningMatchingSideEffect.SuccessMatching -> {
                            // 매칭 성공, 달리기 3초 후 시작
                            Log.d(TAG, "runningMatchingSideEffect: navigate ${args.gameType}")
                            navigate(
                                RunningMatchingFragmentDirections.actionRunningMatchingFragmentToRunningLoadingFragment(
                                    myId = it.userId,
                                    gameType = args.gameType,
                                    roomId = it.roomId,
                                    partnerId = it.partnerId
                                )
                            )
                        }
                        is RunningMatchingSideEffect.SuccessGhostData -> {
                            navigate(RunningMatchingFragmentDirections.actionRunningMatchingFragmentToRunningLoadingFragment(
                                roomId = it.data.id,
                                myId = it.data.userId,
                                partnerId = it.data.partnerId,
                                gameType = it.data.mode
                            ))
                        }
                    }
                }
            }

            launch {
                viewModel.ghostType.collect {
                    viewModel.startGetGhost()
                }
            }
        }
    }


    override fun initAfterBinding() {

    }
}

private const val TAG = "러밋_RunningMatchingFragment"