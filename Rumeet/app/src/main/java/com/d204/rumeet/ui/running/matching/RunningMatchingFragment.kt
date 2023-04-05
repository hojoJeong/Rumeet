package com.d204.rumeet.ui.running.matching

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.NavigationRunningArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningMatchingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
            } else {
                /** 랜덤 매칭 */
                viewModel.startRandomMatching(args.gameType)
            }
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
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
                }
            }
        }
    }


    override fun initAfterBinding() {

    }
}

private const val TAG = "RunningMatchingFragment"