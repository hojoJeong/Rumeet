package com.d204.rumeet.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityMainBinding
import com.d204.rumeet.ui.base.BaseActivity
import com.d204.rumeet.ui.components.RumeetToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main
    private lateinit var navController : NavController

    override fun initStartView() {
        Log.d(TAG, "initStartView: main")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.tbToolbar.visibility = View.GONE
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> {
                    binding.tbToolbar.visibility = View.VISIBLE
                    binding.tbToolbar.setToolbarType(
                        RumeetToolbar.ToolbarType.LOGO_TEXT_ALARM,
                        "홈",
                        rightClickListener = {
                            //Todo 알람 페이지로 navigate
                        })
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
