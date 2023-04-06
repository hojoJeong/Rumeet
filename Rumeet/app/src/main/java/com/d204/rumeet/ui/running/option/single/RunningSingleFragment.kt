package com.d204.rumeet.ui.running.option.single

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningSingleBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.model.RunningDetailType
import com.d204.rumeet.ui.running.option.model.RunningDistance
import com.d204.rumeet.ui.running.option.model.RunningType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningSingleFragment : BaseFragment<FragmentRunningSingleBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_single

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    override fun initStartView() {

    }

    override fun initDataBinding() {
        viewModel.setDistance(RunningDistance.ONE)
    }

    override fun initAfterBinding() {
        initView()
        initDistanceOption()
        initModeOption()
        initGhostOption()
    }

    private fun initView() {
        with(binding.contentMode) {
            tvRunningOptionCompetitionWithTitle.text = "모드를 선택해주세요!"
            btnRunningOptionCompetitionFriend.text = "싱글"
            btnRunningOptionCompetitionRandom.text = "고스트"
            tvRunningOptionCompetitionMessage.visibility = View.GONE
            btnRunningOptionCompetitionRandom.setOnClickListener {
                binding.contentWith.root.visibility = View.VISIBLE
            }
            btnRunningOptionCompetitionFriend.setOnClickListener {
                binding.contentWith.root.visibility = View.GONE
            }
        }

        with(binding.contentWith) {
            tvRunningOptionCompetitionWithTitle.text = "어떤 고스트와 달려볼까요?"
            btnRunningOptionCompetitionFriend.text = "나"
            tvRunningOptionCompetitionMessage.visibility = View.GONE
        }
    }

    private fun initDistanceOption() {
        with(binding.contentDistance) {
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
    }

    private fun initModeOption() {
        with(binding.contentMode) {
            groupButton.addOnButtonCheckedListener { _, checkId, isChecked ->
                if (isChecked) {
                    when (checkId) {
                        R.id.btn_running_option_competition_friend -> {
                            viewModel.setGameType(RunningType.SINGLE)
                        }
                        R.id.btn_running_option_competition_random -> {
                            viewModel.setGameType(RunningType.SINGLE_GHOST)
                        }
                    }
                }

            }
        }
    }

    private fun initGhostOption() {
        with(binding.contentWith) {
            groupButton.addOnButtonCheckedListener { _, checkId, isChecked ->
                if(isChecked){
                    when(checkId){
                        R.id.btn_running_option_competition_friend -> {
                            viewModel.setRunningDetailType(RunningDetailType.GHOST_SINGLE)
                        }
                        R.id.btn_running_option_competition_random -> {
                            viewModel.setRunningDetailType(RunningDetailType.GHOST_FRIEND)
                        }
                    }
                }
            }
        }
    }
}