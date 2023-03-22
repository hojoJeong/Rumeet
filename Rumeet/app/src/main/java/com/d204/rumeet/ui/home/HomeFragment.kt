package com.d204.rumeet.ui.home

import androidx.fragment.app.activityViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel

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
            getUserNameForHome()
            getBestRecordListForHome()
            getBadgeListForHome()
            getRecommendFriendListForHome()
        }

        binding.vm = homeViewModel
    }

    override fun initAfterBinding() {

    }

    companion object {
        private const val TAG = "러밋_HomeFragment"
    }
}
