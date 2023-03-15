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
    }

    private fun initBestRecord() {
        val contentBestRecord = binding.contentHomeBestRecord
        val bestRecordAdapter = ItemBestRecordAdapter().apply {
            //TODO(서버통신 후 체력 데이터 입력)
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

}