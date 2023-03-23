package com.d204.rumeet.ui.join.password

import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinPasswordBinding
import com.d204.rumeet.ui.base.BaseFragment

class JoinPasswordFragment : BaseFragment<FragmentJoinPasswordBinding, JoinPasswordViewModel>(){
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_password

    override val viewModel: JoinPasswordViewModel by viewModels()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}