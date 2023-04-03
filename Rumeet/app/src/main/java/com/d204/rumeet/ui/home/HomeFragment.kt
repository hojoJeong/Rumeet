package com.d204.rumeet.ui.home

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.domain.model.friend.FriendRecommendDomainModel
import com.d204.rumeet.ui.base.*
import com.d204.rumeet.ui.friend.add.AddFriendViewModel
import com.d204.rumeet.ui.friend.list.FriendInfoDialog
import com.d204.rumeet.ui.home.adapter.RecommendFriendAdapter
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
        initBtnClickListener()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.userId.collectLatest {
                    Log.d(TAG, "initDataBinding 유저 아이디: ${viewModel.userId.value.successOrNull()}")
                    viewModel.registFcmToken()
                    viewModel.getRecommendFriendListForHome()
                    viewModel.getHomeData()
                    viewModel.getFriendRecommendList()
                }
            }
            launch {
                viewModel.homeResponse.collectLatest {
                    val response = it.successOrNull()?.badge
                    Log.d(TAG, "initDataBinding 뱃지: $response")
                    if (response != null) {
                        val urlList = resources.getStringArray(R.array.url_badge)
                        val codeList = resources.getStringArray(R.array.code_badge)
                        val myBadgeList = mutableListOf<String>()
                        for (i in response.indices) {
                            myBadgeList.add(urlList[codeList.indexOf(response!![i].code.toString())])
                        }
                        viewModel.setBadgeList(myBadgeList)
                    }
                }
            }
            launch {
                viewModel.friendRecommendList.collectLatest {
                    val response = it.successOrNull()
                    Log.d(TAG, "initDataBinding: $response")
                    initFriendRecommendList(response ?: emptyList())
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    private fun initFriendRecommendList(list: List<FriendRecommendDomainModel>) {
        val recommendAdapter = RecommendFriendAdapter().apply {
            submitList(list)
            homeHandler = object : HomeHandler {
                override fun onClick(userId: Int) {
                    viewModel.getFriendInfo(userId)
                    lifecycleScope.launchWhenResumed {
                        launch {
                            viewModel.friendDetailInfo.collectLatest {
                                Log.d(TAG, "onClick: ${it.successOrNull()}")
                                if (viewModel.friendRecommendList.value.successOrNull() != null)
                                    showFriendInfoDialog(viewModel.friendDetailInfo.value.successOrNull())
                            }
                        }
                    }
                }

            }
        }
        with(binding.rvHomeRecommendFriend) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recommendAdapter
        }


    }

    private fun initBtnClickListener() {
        binding.btnHomeBadge.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentToNavigationMypage("badge"))
        }
        binding.btnHomeRecommendFriend.setOnClickListener {
            viewModel.getFriendRecommendList()
        }
    }


    companion object {
        private const val TAG = "러밋_HomeFragment"
    }

    private fun showFriendInfoDialog(info: FriendInfoDomainModel?) {
        if (info != null) {
            val addFriendViewModel by activityViewModels<AddFriendViewModel>()
            val dialog = FriendInfoDialog().apply {
                viewInfo = "friend"
                friendInfo = info
                friendViewModel = addFriendViewModel
            }
            dialog.show(requireActivity().supportFragmentManager, dialog.tag)
        }
    }
}
