package com.d204.rumeet.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.home.adapter.ItemBestRecordAdapter
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

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

        lifecycleScope.launchWhenResumed {
            homeViewModel.userName.collect{ userName ->
                binding.userName = userName
            }

            homeViewModel.bestRecord.collect{ bestRecordList ->
                initBestRecord(bestRecordList)
            }

            homeViewModel.badgeList.collect{
                initBadgeAdapter(it)
            }

            homeViewModel.recommendFriendList.collect{
                initRecommendFriendAdpater(it)
            }
            homeViewModel.getHomeData()

        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initBestRecord(bestRecordList: List<BestRecordUiModel>) {
        val bestRecordAdapter = ItemBestRecordAdapter().apply {
            //TODO("임시 예외처리")
            if(bestRecordList.isEmpty()){
                val list = mutableListOf<Int>()
                list.add(0)
                list.add(0)
                list.add(0)
                list.add(0)
                submitList(list)
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