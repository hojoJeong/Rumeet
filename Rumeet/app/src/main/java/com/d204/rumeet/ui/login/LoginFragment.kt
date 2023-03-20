package com.d204.rumeet.ui.login

import android.util.Log
import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentLoginBinding
import com.d204.rumeet.ui.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels()

    override fun initStartView() {
        Log.d("TAG", "initStartView: login")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}