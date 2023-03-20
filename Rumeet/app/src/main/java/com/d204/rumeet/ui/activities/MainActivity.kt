package com.d204.rumeet.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityMainBinding
import com.d204.rumeet.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main

    override fun initStartView() {
        Log.d(TAG, "initStartView: main")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
