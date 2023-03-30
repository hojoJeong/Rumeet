package com.d204.rumeet.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityMainBinding
import com.d204.rumeet.ui.base.BaseActivity
import com.d204.rumeet.ui.components.RumeetToolbar
import com.d204.rumeet.util.AMQPManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main
    private lateinit var navController : NavController

    override fun initStartView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

        AMQPManager.initChannel()
    }

    override fun initDataBinding() {
        binding.bvnMain.bvnMain.setupWithNavController(navController)
        binding.bvnMain.bvnMain.selectedItemId = R.id.homeFragment
    }

    override fun initAfterBinding() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.tbToolbar.visibility = View.GONE
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> {
                    binding.bvnMain.root.visibility = View.VISIBLE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.LOGO_TEXT_ALARM, "홈", rightClickListener = {
                                //Todo 알람 페이지로 navigate
                            })
                    }
                }
                R.id.friendListFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.BACK_TEXT, "친구", leftClickListener = {
                            navController.popBackStack()
                        })
                    }
                }
                R.id.addFriendFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.BACK_TEXT, "친구 추가", leftClickListener = {
                            navController.popBackStack()
                        })
                    }
                }
                R.id.chattingListFragment -> {
                    binding.bvnMain.root.visibility = View.VISIBLE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.TEXT, "채팅")
                    }
                }
                R.id.chattingFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.BACK_TEXT, "채팅")
                    }
                }
            }
        }
    }
    
    companion object {
        private const val TAG = "MainActivity"
    }
}
