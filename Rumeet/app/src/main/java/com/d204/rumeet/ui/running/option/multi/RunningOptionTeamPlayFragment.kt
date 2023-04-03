package com.d204.rumeet.ui.running.option.multi

import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionTeamPlayBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.model.RunningDetailType
import com.d204.rumeet.ui.running.option.model.RunningDifficulty
import com.d204.rumeet.ui.running.option.model.RunningDistance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningOptionTeamPlayFragment : BaseFragment<FragmentRunningOptionTeamPlayBinding, RunningViewModel>() {

    override val layoutResourceId: Int get() = R.layout.fragment_running_option_team_play
    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
        with(binding.contentRunningOptionTeamDifficulty){
            groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if(isChecked){
                    when(checkedId){
                        R.id.btn_running_option_difficulty_easy -> {
                            viewModel.setRunningDifficulty(RunningDifficulty.EASY)
                        }
                        R.id.btn_running_option_difficulty_normal -> {
                            viewModel.setRunningDifficulty(RunningDifficulty.NORMAL)
                        }
                        R.id.btn_running_option_difficulty_hard -> {
                            viewModel.setRunningDifficulty(RunningDifficulty.HARD)
                        }
                    }
                }
            }
        }

        with(binding.contentRunningOptionDistance){
            groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if(isChecked){
                    when(checkedId){
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

        with(binding.contentRunningOptionTeamWith){
            groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if(isChecked){
                    when(checkedId){
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