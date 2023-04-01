package com.d204.rumeet.ui.running.loading

import android.os.CountDownTimer
import android.util.Log
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningLoadingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningLoadingFragment : BaseFragment<FragmentRunningLoadingBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_loading

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)
    private val args by navArgs<RunningLoadingFragmentArgs>()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        object : CountDownTimer(3600, 1000) {
            override fun onTick(p0: Long) {
                when ((p0 / 1000).toInt()) {
                    3 -> {
                        binding.tvTime.text = "3"
                        binding.tvTime.animation = animate()
                    }
                    2 -> {
                        binding.tvTime.text = "2"
                        binding.tvTime.animation = animate()
                    }
                    1 -> {
                        binding.tvTime.text = "1"
                        binding.tvTime.animation = animate()
                    }
                }
            }

            override fun onFinish() {
                //navigate
                Log.d(TAG, "onFinish: navigate ${args.gameType}")
                navigate(RunningLoadingFragmentDirections.actionRunningLoadingFragmentToRunningFragment(
                    myId = args.myId,
                    partnerId = args.partnerId,
                    gameType = args.gameType,
                    roomId = args.roomId
                ))
            }
        }.start()
    }

    private fun animate() = ScaleAnimation(
        1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = 500
    }
}

private const val TAG = "RunningLoadingFragment"