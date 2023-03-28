package com.d204.rumeet.ui.mypage

import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMatchingHistoryContainerBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchingHistoryContainerFragment : BaseFragment<FragmentMatchingHistoryContainerBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage)
    override val layoutResourceId: Int
        get() = R.layout.fragment_matching_history_container
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    private lateinit var viewInfo: String

    override fun initStartView() {
        when(viewInfo){
            "ghost" -> initGhostModeView()
            "competition" -> initCompetitionModeView()
            "teamSurvivor" -> initTeamSurvivorModeView()
        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initGhostModeView(){

    }

    private fun initCompetitionModeView(){

    }

    private fun initTeamSurvivorModeView(){
    }

    fun setViewInfo(info: String){
        viewInfo = info
    }

}