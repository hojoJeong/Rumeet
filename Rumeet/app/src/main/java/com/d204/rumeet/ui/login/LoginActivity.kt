package com.d204.rumeet.ui.login

import androidx.activity.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityLoginBinding
import com.d204.rumeet.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class LoginActivity() : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModels()

    override fun initStartView() {
        TODO("Not yet implemented")
    }

    override fun initDataBinding() {
        TODO("Not yet implemented")
    }

    override fun initAfterBinding() {
        TODO("Not yet implemented")
    }
}