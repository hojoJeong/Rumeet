package com.d204.rumeet.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.home.adapter.ItemBestRecordAdapter
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {
    private val homeViewModel by activityViewModels<HomeViewModel>()
    override val layoutResourceId: Int
        get() = R.layout.fragment_home
    override val viewModel: BaseViewModel
        get() = HomeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStartView()
        initAfterBinding()
    }

    override fun initStartView() {
        with(homeViewModel){
            getUserName()
            getBestRecordList()
            getBadgeList()
            getRecommendFriendList()
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
                homeViewModel.userName.collectLatest {

                }
            homeViewModel.bestRecord.collectLatest {

            }

        }
    }

    override fun initAfterBinding() {
    }

    private fun initBestRecord(bestRecordList: List<BestRecordUiModel>) {
        val bestRecordAdapter = ItemBestRecordAdapter().apply {
            //TODO("임시 예외처리")
            if(bestRecordList.isEmpty()){

            } else {
                submitList(bestRecordList)
            }
        }

        with(binding.contentHomeBestRecord){
            tvContentHomeTitle.text = resources.getString(R.string.title_best_record)
            rvContentHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvContentHome.adapter = bestRecordAdapter
        }
    }

    private fun initBadgeAdapter(badgeList: List<String>) {
        if(badgeList.isEmpty()){
            with(binding.contentHomeBadge.tvContentHomeMessage){

            }
        }
    }

    private fun initRecommendFriendAdpater(recommendFriendList: List<RecommendFriendUiModel>) {

    }

    private fun initViewIfUserFirstTime(){
        with(binding){
            contentHomeRecommendFriend.btnContentHome.visibility = View.GONE

            contentHomeBadge.tvContentHomeMessage.text = resources.getString(R.string.content_no_badge)
            contentHomeBadge.tvContentHomeMessage.visibility = View.VISIBLE

            contentHomeRecommendFriend.tvContentHomeMessage.text = resources.getString(R.string.content_no_pace)
            contentHomeRecommendFriend.tvContentHomeMessage.visibility = View.VISIBLE
            contentHomeRecommendFriend.btnContentHome.text = resources.getString(R.string.title_refresh)
            contentHomeRecommendFriend.btnContentHome.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_refresh, 0)
        }
    }
}