package com.d204.rumeet.ui.home

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.util.toCount
import com.d204.rumeet.util.toDistance
import com.d204.rumeet.util.toRecord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_home
    override val viewModel: HomeViewModel by navGraphViewModels<HomeViewModel>(R.id.navigation_main) { defaultViewModelProviderFactory }

    override fun initStartView() {
        binding.lifecycleOwner = viewLifecycleOwner
        with(viewModel) {
            getUserIdByUseCase()
        }
        binding.vm = viewModel
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.userId.collectLatest {
                    Log.d(TAG, "initDataBinding 유저 아이디: ${viewModel.userId.value.successOrNull()}")
                    viewModel.registFcmToken()
                    viewModel.getRecommendFriendListForHome()
                    viewModel.getHomeData()
                }
            }
            launch {
                viewModel.homeResponse.collectLatest {
                    val response = it.successOrNull()?.badge
                    Log.d(TAG, "initDataBinding 뱃지: $response")
                    if(response != null){
                        val urlList = resources.getStringArray(R.array.url_badge)
                        val codeList = resources.getStringArray(R.array.code_badge)
                        val myBadgeList = mutableListOf<String>()
                        for(i in response.indices){
                            myBadgeList.add(urlList[codeList.indexOf(response!![i].code.toString())])
                        }


                        viewModel.setBadgeList(myBadgeList)
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    private fun initAdapter() {
    }


    companion object {
        private const val TAG = "러밋_HomeFragment"
    }
}
