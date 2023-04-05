package com.d204.rumeet.ui.running.option

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionBinding
import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.RunningSoloDomainModel
import com.d204.rumeet.domain.repository.RunningRepository
import com.d204.rumeet.domain.toDomainResult
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningSideEffect
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.adapter.RunningOptionViewPagerAdapter
import com.d204.rumeet.ui.running.option.model.RunningDetailType
import com.d204.rumeet.ui.running.option.model.RunningDifficulty
import com.d204.rumeet.ui.running.option.model.RunningDistance
import com.d204.rumeet.ui.running.option.model.RunningType
import com.d204.rumeet.ui.running.option.multi.RunningOptionCompetitionOrGhostFragment
import com.d204.rumeet.ui.running.option.multi.RunningOptionTeamPlayFragment
import com.d204.rumeet.ui.running.option.single.RunningGhostSingleFragment
import com.d204.rumeet.ui.running.option.single.RunningSingleFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RunningOptionFragment : BaseFragment<FragmentRunningOptionBinding, RunningViewModel>() {

    private val TAG = "러밋_RunningOptionFragment"
    private lateinit var tabList: List<String>
    private lateinit var vpFragmentList: List<Fragment>

    override val layoutResourceId: Int get() = R.layout.fragment_running_option
    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running) { defaultViewModelProviderFactory }

    private val runningType by lazy { arguments?.getInt("type") }

    override fun initStartView() {
        // 싱글
        if (runningType == 1) {
            initSingleModeView()
        }
        // 멀티
        else {
            initMultiModeView()
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenStarted {
            viewModel.runningSideEffect.collectLatest {
                when (it) {
                    is RunningSideEffect.SuccessSoloData -> {
                        if(it.data.partnerId == -1) { // 솔로모드일때 로딩화면으로 이동
                            navigate(
                                RunningOptionFragmentDirections.actionRunningOptionFragmentToRunningLoadingFragment(
                                    myId = it.data.userId,
                                    gameType = it.data.mode,
                                    roomId = it.data.id,
                                    partnerId = it.data.partnerId,
                                    pace = it.data.pace.toIntArray()
                                )
                            )
                        }

                    }
                    else -> {}
                }
            }
        }
    }

    fun startSologame() {

//        suspend fun startSolo(userId: Int, mode: Int, ghost: Int): NetworkResult<RunningSoloDomainModel> {
//            val response = handleApi { runningApiService.startSoloRace(userId, mode, ghost) }.toDomainResult<RunningSoloResponseDto, RunningSoloDomainModel> { it.toDomain()}
//            Log.d("러밋_TAG", "startSolo: $response")
//            return response
//        }
    }

    override fun initAfterBinding() {
        binding.btnRunningOptionStartRunning.setOnClickListener {
            when (viewModel.runningTypeModel.runningType) {
                RunningType.SINGLE -> {  //single
                    Log.d(TAG, "initAfterBinding: 싱글 km mode: ${getRunningType()}")
                    viewModel.startSoloGame(getRunningType())
//                    startSoloGame()
                }
                RunningType.SINGLE_GHOST -> { // ghost
                    Log.d(TAG, "initAfterBinding: 고스트 type: ${getGhostType()}, km mode: ${getRunningType()}")
                    navigate(
                        RunningOptionFragmentDirections.actionRunningOptionFragmentToRunningMatchingFragment(
                            gameType = getRunningType(),
                            ghostType = getGhostType()
                        )
                    )
                }
                else -> { // multi
                    navigate(
                        RunningOptionFragmentDirections.actionRunningOptionFragmentToRunningMatchingFragment(
                            gameType = getRunningType()
                        )
                    )
                }
            }
        }
    }

    private fun initSingleModeView() {
        tabList = listOf(
            "싱글 모드", "고스트 모드"
        )

        vpFragmentList = listOf(
            RunningSingleFragment(),
            RunningGhostSingleFragment(),
        )

        val vpAdapter = RunningOptionViewPagerAdapter(this).apply {
            setFragmentList(vpFragmentList)
        }

        binding.vpgRunningOption.adapter = vpAdapter
        binding.vpgRunningOption.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) viewModel.setGameType(RunningType.SINGLE)
                else viewModel.setGameType(RunningType.SINGLE_GHOST)
            }
        })

        TabLayoutMediator(binding.tblyRunningOption, binding.vpgRunningOption) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun initMultiModeView() {
        tabList = listOf(
            getString(R.string.content_option_competition),
            getString(R.string.content_option_team_play),
        )
        vpFragmentList = listOf(
            RunningOptionCompetitionOrGhostFragment(),
            RunningOptionTeamPlayFragment(),
        )

        val vpAdapter = RunningOptionViewPagerAdapter(this).apply {
            setFragmentList(vpFragmentList)
        }

        binding.vpgRunningOption.adapter = vpAdapter
        binding.vpgRunningOption.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) viewModel.setGameType(RunningType.MULTI_COMPETITIVE)
                else viewModel.setGameType(RunningType.MULTI_COLLABORATION)
            }
        })

        TabLayoutMediator(binding.tblyRunningOption, binding.vpgRunningOption) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun getGhostType(): Int {
        var type = -1
        with(viewModel.runningTypeModel) {
            type = when(this.runningDetailType) {
                RunningDetailType.GHOST_SINGLE -> { // 내 고스트
                    Log.d(TAG, "getGhostType: 내 고스트")
                    2
                }
                RunningDetailType.GHOST_FRIEND -> { // 랜덤 고스트
                    Log.d(TAG, "getGhostType: 랜덤 고스트")
                    1
                }
                else -> {0}
            }
        }
        return type;
    }

    // 난이도와 고스트는 후순위
    private fun getRunningType(): Int {
        var type = -1
        //멀티
        var collabor = 0

        when(viewModel.getRunningDifficulty()){
            RunningDifficulty.EASY -> {
                collabor = 0
            }
            RunningDifficulty.NORMAL -> {
                collabor = 1
            }
            RunningDifficulty.HARD -> {
                collabor = 2
            }
            else -> {}
        }

        with(viewModel.runningTypeModel) {
            when (this.runningType) {
                RunningType.SINGLE_GHOST -> {
                    type = when (this.distance) {
                        RunningDistance.ONE -> {
                            0
                        }
                        RunningDistance.TWO -> {
                            1
                        }
                        RunningDistance.THREE -> {
                            2
                        }
                        RunningDistance.FIVE -> {
                            3
                        }
                    }
                }
                RunningType.SINGLE -> {
                    type = when (this.distance) {
                        RunningDistance.ONE -> {
                            0
                        }
                        RunningDistance.TWO -> {
                            1
                        }
                        RunningDistance.THREE -> {
                            2
                        }
                        RunningDistance.FIVE -> {
                            3
                        }
                    }
                }
                RunningType.MULTI_COLLABORATION -> {
                    type = when (this.distance) {
                        RunningDistance.ONE -> {
                            8 + collabor * 4
                        }
                        RunningDistance.TWO -> {
                            9 + collabor * 4
                        }
                        RunningDistance.THREE -> {
                            10 + collabor * 4
                        }
                        RunningDistance.FIVE -> {
                            11 + collabor * 4
                        }
                    }
                }
                RunningType.MULTI_COMPETITIVE -> {
                    type = when (this.distance) {
                        RunningDistance.ONE -> {
                            4
                        }
                        RunningDistance.TWO -> {
                            5
                        }
                        RunningDistance.THREE -> {
                            6
                        }
                        RunningDistance.FIVE -> {
                            7
                        }
                    }
                }
            }
        }
        return type
    }

}