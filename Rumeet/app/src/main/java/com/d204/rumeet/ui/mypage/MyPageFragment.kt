package com.d204.rumeet.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMyPageBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MypageViewModel>() {
    override val layoutResourceId: Int get() = R.layout.fragment_my_page
    override val viewModel: MypageViewModel by viewModels()

    override fun initStartView() {

    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
        binding.tvMypage.setOnClickListener {
            navigate(MyPageFragmentDirections.actionMyPageFragmentToSettingFragment())
        }
        binding.tvSearch.setOnClickListener {
            navigate(MyPageFragmentDirections.actionMyPageFragmentToFriendListFragment())
        }
    }
}