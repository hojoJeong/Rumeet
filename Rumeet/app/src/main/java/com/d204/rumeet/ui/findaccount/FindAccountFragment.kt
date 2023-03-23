package com.d204.rumeet.ui.findaccount

import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentFindAccountBinding
import com.d204.rumeet.ui.base.BaseFragment

class FindAccountFragment : BaseFragment<FragmentFindAccountBinding, FindAccountViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_find_account

    override val viewModel: FindAccountViewModel by navGraphViewModels(R.id.navigation_join)

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}