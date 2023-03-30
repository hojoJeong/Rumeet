package com.d204.rumeet.ui.running.option.multi

import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionTeamPlayBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel


class RunningOptionTeamPlayFragment : BaseFragment<FragmentRunningOptionTeamPlayBinding, BaseViewModel>() {

    override val layoutResourceId: Int get() = R.layout.fragment_running_option_team_play
    override val viewModel: BaseViewModel get() = RunningViewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

}