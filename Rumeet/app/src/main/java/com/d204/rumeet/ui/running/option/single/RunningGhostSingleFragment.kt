package com.d204.rumeet.ui.running.option.single

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningSingleGhostBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.model.RunningDetailType
import com.d204.rumeet.ui.running.option.model.RunningDistance
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "러밋_RunningGhostSingleFragm"
@AndroidEntryPoint
class RunningGhostSingleFragment : BaseFragment<FragmentRunningSingleGhostBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_single_ghost

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)


    override fun initStartView() {
        Log.d("TAG", "initStartView: test")
    }

    override fun initDataBinding() {
        viewModel.setRunningDetailType(RunningDetailType.GHOST_SINGLE) // default : 내 고스트

    }

    override fun initAfterBinding() {
        binding.contentSelectRunner.btnRunningOptionCompetitionFriend.text = "나"
        binding.contentSelectRunner.btnRunningOptionCompetitionRandom.text = "랜덤"
        binding.contentSelectRunner.tvRunningOptionCompetitionMessage.visibility = View.GONE

        with(binding.contentSelectRunner) {
            groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if(isChecked) {
                    when(checkedId) {
                        R.id.btn_running_option_competition_friend -> { // 내 고스트
                            Log.d(TAG, "initAfterBinding: 내 고스트 클릭")
                            viewModel.setRunningDetailType(RunningDetailType.GHOST_SINGLE)
                        }
                        R.id.btn_running_option_competition_random -> { // 랜덤 고스트
                            Log.d(TAG, "initAfterBinding: 랜덤 고스트 클릭")
                            viewModel.setRunningDetailType(RunningDetailType.GHOST_FRIEND)
                        }
                    }
                }
            }
        }

        with(binding.contentDistance) {
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

    }
}