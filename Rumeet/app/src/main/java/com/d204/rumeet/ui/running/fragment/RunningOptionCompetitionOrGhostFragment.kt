package com.d204.rumeet.ui.running.fragment

import android.view.View
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionCompetitionOrGhostBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel

class RunningOptionCompetitionOrGhostFragment :
    BaseFragment<FragmentRunningOptionCompetitionOrGhostBinding, BaseViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_option_competition_or_ghost
    override val viewModel: BaseViewModel
        get() = RunningViewModel()

    override fun initStartView() {
        initMutiMode()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initSingleMode() {
        binding.contentRunningOptionWith.btnRunningOptionCompetitionFriend.text = "나"
        binding.contentRunningOptionWith.tvRunningOptionCompetitionMessage.visibility = View.GONE
    }

    private fun initMutiMode() {
        with(binding.contentRunningOptionWith.tvRunningOptionCompetitionMessage) {
            binding.contentRunningOptionWith.btnRunningOptionCompetitionFriend.setOnClickListener {
                text = getString(R.string.content_message_selected_friend)
            }

            binding.contentRunningOptionWith.btnRunningOptionCompetitionRandom.setOnClickListener {
                text = getString(R.string.content_message_selected_random)
            }
        }
    }
}