package com.d204.rumeet.ui.mypage

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMatchingHistoryContainerBinding
import com.d204.rumeet.domain.model.user.MatchingHistoryRaceListDomainModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.adapter.MatchingHistoryItemAdapter
import com.d204.rumeet.ui.mypage.model.MatchingHistoryRaceUiModel
import com.d204.rumeet.ui.mypage.model.MatchingHistoryUiModel
import com.d204.rumeet.ui.mypage.model.toUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchingHistoryContainerFragment : BaseFragment<FragmentMatchingHistoryContainerBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_matching_history_container
    override val viewModel: MyPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage){defaultViewModelProviderFactory}

    private lateinit var matchingList: List<MatchingHistoryRaceUiModel>


    override fun initStartView() {
        initView()
        binding.contentMatchintHistoryNoResult.tvContentNoResultMessage.text = "매칭 기록이 없습니다."
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
    }


    fun setViewInfo(list: List<MatchingHistoryRaceUiModel>){
        matchingList = list ?: emptyList<MatchingHistoryRaceUiModel>()
    }

    private fun initView(){
        if(matchingList.size > 0){
            binding.contentMatchintHistoryNoResult.root.visibility = View.GONE
        }
        val matchingAdapter = MatchingHistoryItemAdapter(this.childFragmentManager).apply {
            submitList(matchingList)
        }

        with(binding.rvMatchingHistoryContainer){
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = matchingAdapter
        }
    }

}