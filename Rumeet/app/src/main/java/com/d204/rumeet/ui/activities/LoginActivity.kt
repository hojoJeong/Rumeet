package com.d204.rumeet.ui.activities

import androidx.activity.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityLoginBinding
import com.d204.rumeet.ui.base.BaseActivity
import com.d204.rumeet.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModels()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}