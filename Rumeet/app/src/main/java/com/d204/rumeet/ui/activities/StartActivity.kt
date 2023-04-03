package com.d204.rumeet.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityStartBinding
import com.d204.rumeet.domain.usecase.user.RegistFcmTokenUseCase
import com.d204.rumeet.ui.base.BaseActivity
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>() {
    override val layoutResourceId: Int = R.layout.activity_start
    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}