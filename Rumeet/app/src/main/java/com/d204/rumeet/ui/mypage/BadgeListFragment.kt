package com.d204.rumeet.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentBadgeListBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel

class BadgeListFragment : BaseFragment<FragmentBadgeListBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_badge_list
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

}