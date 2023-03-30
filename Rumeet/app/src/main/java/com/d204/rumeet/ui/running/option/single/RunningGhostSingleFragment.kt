package com.d204.rumeet.ui.running.option.single

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningSingleGhostBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningGhostSingleFragment : BaseFragment<FragmentRunningSingleGhostBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_single_ghost

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    override fun initStartView() {
        Log.d("TAG", "initStartView: test")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.contentSelectRunner.btnRunningOptionCompetitionFriend.text = "나"
        binding.contentSelectRunner.btnRunningOptionCompetitionRandom.text = "랜덤"
        binding.contentSelectRunner.tvRunningOptionCompetitionMessage.visibility = View.GONE
    }
}