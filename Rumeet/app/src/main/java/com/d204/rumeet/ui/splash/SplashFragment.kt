package com.d204.rumeet.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
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

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_splash
    override val viewModel: SplashViewModel by viewModels()

    private val permissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val allCheck = isGranted.all { it.value }
            if (allCheck) {
                viewModel.checkAppState()
            } else {
                toastMessage("권한을 설정해주세요")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android 12 부터 SplashScreen으로 대체
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            initSplashScreen()
        }
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.checkAppState()
        } else {
            permissionLauncher.launch(permissionList)
        }
    }

    override fun initStartView() {
        viewModel.checkAppState()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.navigationEvent.collectLatest { state ->
                when (state) {
                    is SplashNavigationAction.NavigateOnBoarding -> navigate(
                        SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                    )
                    is SplashNavigationAction.StartLoginActivity -> requireContext().startActivityAfterClearBackStack(
                        LoginActivity::class.java
                    )
                    is SplashNavigationAction.StartMainActivity -> requireContext().startActivityAfterClearBackStack(
                        MainActivity::class.java
                    )
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