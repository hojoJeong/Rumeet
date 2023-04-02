package com.d204.rumeet.ui.running.finish

import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningFinishBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningFinishFragment : BaseFragment<FragmentRunningFinishBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_finish

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    private val args by navArgs<RunningFinishFragmentArgs>()
    private val locationList by lazy { args.locationList }
    private val runningResult by lazy { args.result }

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.tvRunningCalorie.text = runningResult.calorie.toString()
        binding.tvRunningHeight.text = runningResult.height.toString()
        binding.tvRunningPace.text = runningResult.velocity.toString()
        binding.tvResult.text = if(runningResult.success == 0) "패배" else "승리"

        binding.btnOkay.setContent("확인")
        binding.btnOkay.addClickListener{
            navigate(RunningFinishFragmentDirections.actionRunningFinishFragmentToHomeFragment())
        }
    }
}