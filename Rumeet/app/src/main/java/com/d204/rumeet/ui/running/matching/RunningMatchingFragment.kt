package com.d204.rumeet.ui.running.matching

import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.NavigationRunningArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningMatchingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import kotlin.math.log


@AndroidEntryPoint
class RunningMatchingFragment :
    BaseFragment<FragmentRunningMatchingBinding, RunningMatchingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_matching

    override val viewModel: RunningMatchingViewModel by activityViewModels<RunningMatchingViewModel>()
    private val runningViewModel by viewModels<RunningViewModel>()

    private val args by navArgs<RunningMatchingFragmentArgs>()

    override fun initStartView() {
        Log.d(TAG, "initStartView: runningMAtchingFragment withfriend: ${args.withFriend}")
        binding.lifecycleOwner = viewLifecycleOwner

        /** 초대 받은 사람인 경우 */
        if (args.invitedFromFriend) {
            Log.d(TAG, "initStartView: 초대 받은 사람")
            Log.d(TAG, "initStartView: ${args.gameType}, ${args.roomId}")
            viewModel.subscribeFriendQueue(args.roomId, args.myId)
            runningViewModel.acceptRunningRequest(raceId = args.roomId)
        } else {
            if (args.withFriend) {
                /** 초대 한 사람인 경우 */
                viewModel.startFriendModeMatching(args.gameType)
            } else if (args.ghostType != -1) { // 고스트 모드 분기 (내 고스트:1, 랜덤 고스트 : 2)
                Log.d(TAG, "initStartView: 고스트 모드임!! (1:나, 2:랜덤) : ${args.ghostType}")
                viewModel.startGhost(args.gameType, args.ghostType)
            } else {
                /** 랜덤 매칭 */
                viewModel.startRandomMatching(args.gameType)
            }
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
                                    partnerId = it.partnerId,
                                    pace = IntArray(3)
                                )
                            )
                        }
                        is RunningMatchingSideEffect.SuccessGhostData -> {
                            navigate(RunningMatchingFragmentDirections.actionRunningMatchingFragmentToRunningLoadingFragment(
                                roomId = it.data.id,
                                myId = it.data.userId,
                                partnerId = it.data.partnerId,
                                gameType = it.data.mode,
                                pace = it.data.pace.toIntArray()
                            ))
                        }
                    }
                }
            }

            launch {
                viewModel.ghostType.collect {
                    if((viewModel.gameType.value<4) && (viewModel.ghostType.value > 0)) // 고스트 모드일때만
                        viewModel.startGetGhost()
                }
            }
        }
    }


    override fun initAfterBinding() {

    }
}

private const val TAG = "RunningMatchingFragment"