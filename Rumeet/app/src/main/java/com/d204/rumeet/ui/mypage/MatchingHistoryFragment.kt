package com.d204.rumeet.ui.mypage

import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMatchingHistoryBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.adapter.MatchingHistoryViewPagerAdapter
import com.d204.rumeet.ui.mypage.model.MatchingHistoryRaceUiModel
import com.d204.rumeet.ui.mypage.model.toUiModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchingHistoryFragment : BaseFragment<FragmentMatchingHistoryBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_matching_history
    override val viewModel: MyPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage){defaultViewModelProviderFactory}
    private lateinit var ghostList : MutableList<MatchingHistoryRaceUiModel>
    private lateinit var competitionList : MutableList<MatchingHistoryRaceUiModel>
    private lateinit var teamList : MutableList<MatchingHistoryRaceUiModel>

    override fun initStartView() {
    }

    override fun initDataBinding() {
        viewModel.getMatchingHistoryList()
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.matchingHistoryList.collect { response ->
                    val raceList = response.successOrNull()?.raceList ?: emptyList()
                    ghostList = mutableListOf<MatchingHistoryRaceUiModel>()
                    competitionList = mutableListOf<MatchingHistoryRaceUiModel>()
                    teamList = mutableListOf<MatchingHistoryRaceUiModel>()

                    for(race in raceList){
                        when (race.mode) {
                            0, 1, 2, 3 -> {
                                ghostList.add(race.toUiModel())
                            }
                            4, 5, 6, 7 -> {
                                competitionList.add(race.toUiModel())
                            }
                            else -> {
                                teamList.add(race.toUiModel())
                            }
                        }
                    }

                    val summaryData = response.successOrNull()?.summaryData
                    binding.data = summaryData?.toUiModel()
                    initView()
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initView(){
        val ghostModeFragment = MatchingHistoryContainerFragment().apply {
            setViewInfo(ghostList)
        }
        val competitionModeFragment = MatchingHistoryContainerFragment().apply {
            setViewInfo(competitionList)
        }
        val teamSurvivorModeFragment = MatchingHistoryContainerFragment().apply {
            setViewInfo(teamList)
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