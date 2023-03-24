package com.d204.rumeet.ui.join.nickname

import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinNicknameBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.join.JoinViewModel

class JoinNickNameFragment : BaseFragment<FragmentJoinNicknameBinding, JoinViewModel>(){
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_nickname

    override val viewModel: JoinViewModel by navGraphViewModels(R.id.navigation_join)

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}