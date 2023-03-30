package com.d204.rumeet.ui.running.option.multi

import android.util.Log
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionCompetitionOrGhostBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.model.RunningDetailType
import com.d204.rumeet.ui.running.option.model.RunningDistance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningOptionCompetitionOrGhostFragment :
    BaseFragment<FragmentRunningOptionCompetitionOrGhostBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_option_competition_or_ghost

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    override fun initStartView() {
        initMultiMode()
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    private fun initMultiMode() {
        with(binding.contentRunningOptionWith.tvRunningOptionCompetitionMessage) {
            binding.contentRunningOptionWith.btnRunningOptionCompetitionFriend.setOnClickListener {
                text = getString(R.string.content_message_selected_friend)
            }

            binding.contentRunningOptionWith.btnRunningOptionCompetitionRandom.setOnClickListener {
                text = getString(R.string.content_message_selected_random)
            }
        }

        with(binding.contentRunningOptionDistance) {
            groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.btn_competition_1km -> {
                            viewModel.setDistance(RunningDistance.ONE)
                        }
                        R.id.btn_competition_2km -> {
                            viewModel.setDistance(RunningDistance.TWO)
                        }
                        R.id.btn_competition_3km -> {
                            viewModel.setDistance(RunningDistance.THREE)
                        }
                        R.id.btn_competition_5km -> {
                            viewModel.setDistance(RunningDistance.FIVE)
                        }
                    }
                }
            }
        }

        with(binding.contentRunningOptionWith) {
            groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.btn_running_option_competition_friend -> {
                            viewModel.setRunningDetailType(RunningDetailType.FRIEND)
                        }
                        R.id.btn_running_option_competition_random -> {
                            viewModel.setRunningDetailType(RunningDetailType.RANDOM)
                        }
                    }
                }
            }
        }
    }
}