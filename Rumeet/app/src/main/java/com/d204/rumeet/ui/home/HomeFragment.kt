package com.d204.rumeet.ui.home

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_home
    override val viewModel: HomeViewModel by navGraphViewModels<HomeViewModel>(R.id.navigation_main) { defaultViewModelProviderFactory }

    override fun initStartView() {
        with(viewModel) {
            getUserIdByUseCase()
            getHomeData()
            getBestRecordListForHome()
            getBadgeListForHome()
            getRecommendFriendListForHome()
        }
        binding.vm = viewModel
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.userId.collect{
                    viewModel.registFcmToken()
                }
            }
        }
    }

    override fun initAfterBinding() {

    }



    companion object {
        private const val TAG = "러밋_HomeFragment"
    }
}
