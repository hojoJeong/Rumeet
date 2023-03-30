package com.d204.rumeet.ui.running.option

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.option.adapter.RunningOptionViewPagerAdapter
import com.d204.rumeet.ui.running.option.multi.RunningOptionCompetitionOrGhostFragment
import com.d204.rumeet.ui.running.option.multi.RunningOptionTeamPlayFragment
import com.d204.rumeet.ui.running.option.single.RunningGhostSingleFragment
import com.d204.rumeet.ui.running.option.single.RunningSingleFragment
import com.google.android.material.tabs.TabLayoutMediator

class RunningOptionFragment : BaseFragment<FragmentRunningOptionBinding, RunningViewModel>() {

    private lateinit var tabList: List<String>
    private lateinit var vpFragmentList: List<Fragment>

    override val layoutResourceId: Int get() = R.layout.fragment_running_option
    override val viewModel: RunningViewModel by viewModels()

    private val args by navArgs<RunningOptionFragmentArgs>()

    override fun initStartView() {
        // 싱글
        if (args.runningType == 1) initSingleModeView()
        // 멀티
        else initMultiModeView()
        setRunningStartBtnClickListener()
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    private fun initSingleModeView() {
        tabList = listOf(
            "싱글 모드","고스트 모드"
        )

        vpFragmentList = listOf(
            RunningSingleFragment(),
            RunningOptionCompetitionOrGhostFragment(),
        )

        val vpAdapter = RunningOptionViewPagerAdapter(this).apply {
            setFragmentList(vpFragmentList)
        }

        binding.vpgRunningOption.adapter = vpAdapter
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

        TabLayoutMediator(binding.tblyRunningOption, binding.vpgRunningOption) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun setRunningStartBtnClickListener() {
        binding.btnRunningOptionStartRunning.setOnClickListener {
            navigate(RunningOptionFragmentDirections.actionRunningOptionFragmentToRunningMatchingFragment())
        }
    }
}