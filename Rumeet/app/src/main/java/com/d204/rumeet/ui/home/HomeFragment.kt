package com.d204.rumeet.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.home.adapter.BestRecordAdapter
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.home.adapter.BadgeAdapter
import com.d204.rumeet.ui.home.adapter.RecommendFriendAdapter
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {
    private val homeViewModel by activityViewModels<HomeViewModel>()
    override val layoutResourceId: Int
        get() = R.layout.fragment_home
    override val viewModel: BaseViewModel
        get() = HomeViewModel()

    override fun initDataBinding() {
    }

    override fun initStartView() {
        with(homeViewModel) {
            getUserNameForWelcomeMessage()
            getBestRecordList()
            getBadgeList()
            getRecommendFriendList()
        }

        binding.vm = homeViewModel
    }

    override fun initAfterBinding() {
    }

    private fun initCardviewTitleAndButton(){

    }




    companion object {
        private const val TAG = "러밋_HomeFragment"
    }
}
