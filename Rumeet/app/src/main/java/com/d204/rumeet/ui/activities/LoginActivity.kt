package com.d204.rumeet.ui.activities

import android.util.Log
import androidx.activity.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityLoginBinding
import com.d204.rumeet.ui.base.BaseActivity
import com.d204.rumeet.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override val layoutResourceId: Int = R.layout.activity_login

    override fun initStartView() {
        Log.d(TAG, "initStartView: login")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
    companion object{
        private const val TAG = "LoginActivity"
    }
}