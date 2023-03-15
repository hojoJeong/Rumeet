package com.d204.rumeet.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivitySplashBinding
import com.d204.rumeet.ui.activities.MainActivity
import com.d204.rumeet.ui.base.BaseActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override val layoutResourceId: Int = R.layout.activity_splash
    override val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android 12 부터 SplashScreen으로 대체
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            initSplashScreen()
        }else{
            setContentView(binding.root)
        }
    }

    override fun initStartView() {
        viewModel.checkVersion()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.appVersion.collectLatest {
                    if(it == 0) getToken()
                }
            }

            launch {
                viewModel.navigateToHome.collectLatest {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    /** FB 토큰 갱신 */
    private fun getToken(){

    }

    /** 스플래시 스크린 생성 */
    private fun initSplashScreen(){
        val content : View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.splashScreenGone.value) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )
    }
}