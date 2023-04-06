package com.d204.rumeet.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


    private val permissionList = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.FOREGROUND_SERVICE
        )
    }else{
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
        )
    }


    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val allCheck = isGranted.all { it.value }
            if (allCheck) {
                viewModel.checkAppState()
            } else {
                toastMessage("권한을 설정해주세요")
                requireActivity().finishAffinity()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android 12 부터 SplashScreen으로 대체
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            initSplashScreen()
        }

        // R버전 아래부터는 구버전으로 권한요청
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                viewModel.checkAppState()
            } else{
                permissionLauncher.launch(permissionList)
            }
        }
        // 구버전으로 권한 요청
        else{
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                viewModel.checkAppState()
            } else{
                permissionLauncher.launch(permissionList)
            }
        }
    }

    override fun initStartView() {

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
                    is SplashNavigationAction.StartMainActivity -> {
                        if (requireActivity().intent.extras?.getString("type") != null) {
                            receiveFcmMessageOnBackGround()
                        } else {
                            requireContext().startActivityAfterClearBackStack(
                                MainActivity::class.java
                            )
                        }
                    }
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

    private fun receiveFcmMessageOnBackGround() {
        val type = requireActivity().intent.extras?.getString("type")
        val startActivityIntent = Intent(requireContext(), MainActivity::class.java).apply {
            putExtra("type", type)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        requireActivity().startActivity(startActivityIntent)
    }
}