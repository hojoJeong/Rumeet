package com.d204.rumeet.ui.splash

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSplashBinding
import com.d204.rumeet.ui.activities.LoginActivity
import com.d204.rumeet.ui.activities.MainActivity
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.util.startActivityAfterClearBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_splash
    override val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android 12 부터 SplashScreen으로 대체
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            initSplashScreen()
        }
    }

    override fun initStartView() {
        viewModel.checkAppState()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.navigationEvent.collectLatest { state ->
                when(state){
                    is SplashNavigationAction.NavigateOnBoarding -> navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
                    is SplashNavigationAction.StartLoginActivity -> requireContext().startActivityAfterClearBackStack(LoginActivity::class.java)
                    is SplashNavigationAction.StartMainActivity -> requireContext().startActivityAfterClearBackStack(MainActivity::class.java)
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    /** FB 토큰 갱신 */
    private fun getToken() {

    }

    /** 스플래시 스크린 생성 */
    private fun initSplashScreen() {
        val content: View = requireActivity().findViewById(android.R.id.content)
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