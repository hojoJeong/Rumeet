package com.d204.rumeet.ui.join.nickname

import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinNicknameBinding
import com.d204.rumeet.ui.base.BaseFragment

class JoinNicknameFragment : BaseFragment<FragmentJoinNicknameBinding, JoinNicknameViewModel>(){
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_nickname

    override val viewModel: JoinNicknameViewModel by viewModels()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}