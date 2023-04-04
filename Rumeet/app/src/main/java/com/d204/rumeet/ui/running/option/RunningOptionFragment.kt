package com.d204.rumeet.ui.running.option

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.adapter.RunningOptionViewPagerAdapter
import com.d204.rumeet.ui.running.option.model.RunningDetailType
import com.d204.rumeet.ui.running.option.model.RunningDistance
import com.d204.rumeet.ui.running.option.model.RunningType
import com.d204.rumeet.ui.running.option.multi.RunningOptionCompetitionOrGhostFragment
import com.d204.rumeet.ui.running.option.multi.RunningOptionTeamPlayFragment
import com.d204.rumeet.ui.running.option.single.RunningGhostSingleFragment
import com.d204.rumeet.ui.running.option.single.RunningSingleFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningOptionFragment : BaseFragment<FragmentRunningOptionBinding, RunningViewModel>() {

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

    }

    override fun initAfterBinding() {
        binding.btnRunningOptionStartRunning.setOnClickListener {
            //single
            if (viewModel.runningTypeModel.runningType == RunningType.SINGLE) {

            } else {
                if (viewModel.runningTypeModel.runningDetailType == RunningDetailType.FRIEND) {
                    Log.d("TAG", "initAfterBinding: 친구 모드")
                    navigate(
                        RunningOptionFragmentDirections.actionRunningOptionFragmentToSelectFriendFragment(
                            gameType = getRunningType()
                        )
                    )
                } else {
                    Log.d("TAG", "initAfterBinding: 랜덤 모드")
                    navigate(
                        RunningOptionFragmentDirections.actionRunningOptionFragmentToRunningMatchingFragment(
                            withFriend = false, gameType = getRunningType()
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

    // 난이도와 고스트는 후순위
    private fun getRunningType(): Int {
        var type = -1
        //멀티
        with(viewModel.runningTypeModel) {
            when (this.runningType) {
                RunningType.SINGLE_GHOST -> {
                    // 추가 구현 필요
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
                            8
                        }
                        RunningDistance.TWO -> {
                            9
                        }
                        RunningDistance.THREE -> {
                            10
                        }
                        RunningDistance.FIVE -> {
                            11
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