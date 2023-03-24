package com.d204.rumeet.ui.running.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.adapter.RunningOptionViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class RunningOptionFragment: BaseFragment<FragmentRunningOptionBinding, BaseViewModel>() {

    private val runningViewModel by viewModels<RunningViewModel>()
    private lateinit var tabList: List<String>
    private lateinit var vpFragmentList: List<Fragment>
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_option
    override val viewModel: BaseViewModel
        get() = runningViewModel

    override fun initStartView() {
        //TODO(싱글/멀티 분기 처리)
        initMutiModeView()
        setRunningStartBtnClickListener()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initMutiModeView(){
        tabList = listOf(
            getString(R.string.content_option_competition),
            getString(R.string.content_option_team_play),
            getString(R.string.content_option_relay)
        )
        vpFragmentList = listOf(RunningOptionCompetitionOrGhostFragment(),
            RunningOptionTeamPlayFragment(),
            RunningOptionRelayFragment()
            )

        val vpAdapter= RunningOptionViewPagerAdapter(this).apply {
            setFragmentList(vpFragmentList)
            setTabCount(tabList.size)
        }

        binding.vpgRunningOption.adapter = vpAdapter

        TabLayoutMediator(binding.tblyRunningOption, binding.vpgRunningOption){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun setRunningStartBtnClickListener(){
        binding.btnRunningOptionStartRunning.setOnClickListener{
            navigate(RunningOptionFragmentDirections.actionRunningOptionContainerFragmentToRunningFragment())
        }
    }



}