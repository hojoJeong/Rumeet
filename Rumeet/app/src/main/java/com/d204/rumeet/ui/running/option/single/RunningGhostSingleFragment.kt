package com.d204.rumeet.ui.running.option.single

import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningSingleGhostBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel

class RunningGhostSingleFragment : BaseFragment<FragmentRunningSingleGhostBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_single_ghost

    override val viewModel: RunningViewModel by viewModels()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.contentSelectRunner.btnRunningOptionCompetitionFriend.text = "나"
        binding.contentSelectRunner.btnRunningOptionCompetitionRandom.text = "랜덤"
    }
}