package com.d204.rumeet.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMatchingHistoryBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.adapter.MatchingHistoryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MatchingHistoryFragment : BaseFragment<FragmentMatchingHistoryBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)
    override val layoutResourceId: Int
        get() = R.layout.fragment_matching_history
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView(){
        val ghostModeFragment = MatchingHistoryContainerFragment().apply {
            setViewInfo("ghost")
        }
        val competitionModeFragment = MatchingHistoryContainerFragment().apply {
            setViewInfo("competition")
        }
        val teamSurvivorModeFragment = MatchingHistoryContainerFragment().apply {
            setViewInfo("teamSurvivor")
        }

        val fragmentList = listOf(
            ghostModeFragment,
            competitionModeFragment,
            teamSurvivorModeFragment
        )

        val tabList = listOf(
            getString(R.string.title_mode_ghost),
            getString(R.string.title_mode_competition),
            getString(R.string.title_mode_team)
        )

        binding.vpMatchingHistory.adapter = MatchingHistoryViewPagerAdapter(this).apply {
            setFragmentList(fragmentList)
        }
        TabLayoutMediator(binding.tblyMatchingHistory, binding.vpMatchingHistory){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

}