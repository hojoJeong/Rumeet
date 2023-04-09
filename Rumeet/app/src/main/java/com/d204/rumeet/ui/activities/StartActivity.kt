package com.d204.rumeet.ui.activities

import androidx.appcompat.app.AppCompatDelegate
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityStartBinding
import com.d204.rumeet.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>() {
    override val layoutResourceId: Int = R.layout.activity_start
    override fun initStartView() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}