package com.d204.rumeet.ui.activities

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityLoginBinding
import com.d204.rumeet.ui.base.BaseActivity
import com.d204.rumeet.ui.components.RumeetToolbar
import com.d204.rumeet.ui.join.JoinViewModel
import com.d204.rumeet.ui.join.JoinViewModel_Factory
import com.d204.rumeet.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override val layoutResourceId: Int = R.layout.activity_login
    private lateinit var navController: NavController

    override fun initStartView() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Log.d(TAG, "initStartView: login")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_login) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.tbToolbar.visibility = View.GONE
            when (navController.currentDestination?.id) {
                R.id.joinNickNameFragment -> {
                    binding.tbToolbar.visibility = View.VISIBLE
                    binding.tbToolbar.setToolbarType(
                        RumeetToolbar.ToolbarType.BACK_TEXT,
                        "회원가입",
                        leftClickListener = {
                            navController.popBackStack()
                        })
                }
                R.id.findAccountFragment -> {
                    binding.tbToolbar.visibility = View.VISIBLE
                    binding.tbToolbar.setToolbarType(
                        RumeetToolbar.ToolbarType.BACK_TEXT,
                        "내 계정 찾기",
                        leftClickListener = {
                            navController.popBackStack()
                        })
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}