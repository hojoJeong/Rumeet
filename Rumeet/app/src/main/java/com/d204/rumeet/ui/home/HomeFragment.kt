package com.d204.rumeet.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentHomeBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.home.adapter.ItemBestRecordAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {
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
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
        binding.name = "배달전문 박정은"
        initBestRecord()
        initBadgeAdapter()
        initRecommendFriendAdpater()
    }

    private fun initBestRecord() {
        val contentBestRecord = binding.contentHomeBestRecord
        val bestRecordAdapter = ItemBestRecordAdapter().apply {

        }

        with(binding.contentHomeBestRecord){
            tvContentHomeTitle.text = resources.getString(R.string.title_best_record)
            rvContentHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvContentHome.adapter = bestRecordAdapter
        }
    }

    private fun initBadgeAdapter() {

    }

    private fun initRecommendFriendAdpater() {

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