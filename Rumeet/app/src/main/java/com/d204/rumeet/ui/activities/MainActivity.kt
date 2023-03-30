package com.d204.rumeet.ui.activities

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ActivityMainBinding
import com.d204.rumeet.ui.base.BaseActivity
import com.d204.rumeet.ui.components.RumeetToolbar
import com.d204.rumeet.ui.home.HomeFragmentDirections
import com.d204.rumeet.util.amqp.ChattingAMQPMananer
import com.d204.rumeet.util.amqp.RunningAMQPManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main
    private lateinit var navController: NavController

    private var flag = true

    override fun initStartView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

        ChattingAMQPMananer.initChannel()
        RunningAMQPManager.initChannel()
    }

    override fun initDataBinding() {
        binding.bvnMain.bvnMain.setupWithNavController(navController)
        binding.bvnMain.bvnMain.selectedItemId = R.id.homeFragment

        binding.bvnMain.btnRunning.setOnClickListener {
            runningState(flag)
        }

        binding.btnSingle.setOnClickListener {
            navController.navigate(R.id.navigation_running, bundleOf("type" to 1))
        }

        binding.btnMulti.setOnClickListener {
            navController.navigate(R.id.navigation_running, bundleOf("type" to 2))
        }
    }

    private fun runningState(state: Boolean) {
        if (state) {
            binding.fcvMain.isClickable = false
            binding.lyRunning.visibility = View.VISIBLE
        } else {
            binding.fcvMain.isClickable = true
            binding.lyRunning.visibility = View.GONE
        }
        flag = !flag
    }

    override fun initAfterBinding() {
        navController.addOnDestinationChangedListener { _, _, _ ->

            binding.fcvMain.isClickable = true
            binding.lyRunning.visibility = View.GONE

            binding.tbToolbar.visibility = View.GONE
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> {
                    binding.bvnMain.root.visibility = View.VISIBLE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(
                            RumeetToolbar.ToolbarType.LOGO_TEXT_ALARM,
                            "홈",
                            rightClickListener = {
                                //Todo 알람 페이지로 navigate
                            })
                    }
                }
                R.id.friendListFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(
                            RumeetToolbar.ToolbarType.BACK_TEXT,
                            "친구",
                            leftClickListener = {
                                navController.popBackStack()
                            })
                    }
                }
                R.id.addFriendFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(
                            RumeetToolbar.ToolbarType.BACK_TEXT,
                            "친구 추가",
                            leftClickListener = {
                                navController.popBackStack()
                            })
                    }
                }
                R.id.chattingListFragment -> {
                    binding.bvnMain.root.visibility = View.VISIBLE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.TEXT, "채팅 목록")
                    }
                }
                R.id.chattingFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                    with(binding.tbToolbar) {
                        visibility = View.VISIBLE
                        setToolbarType(RumeetToolbar.ToolbarType.BACK_TEXT, "채팅")
                    }
                }

                R.id.runningOptionFragment -> {
                    binding.bvnMain.root.visibility = View.VISIBLE
                }

                R.id.runningFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                }

                R.id.runningMatchingFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                }
                R.id.runningLoadingFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                }
                R.id.runningMatchingFailFragment -> {
                    binding.bvnMain.root.visibility = View.GONE
                }

            }
        }
    }

    override fun onBackPressed() {
        if (findNavController(R.id.fcv_main).currentDestination?.id == R.id.runningMatchingFailFragment) {
            findNavController(R.id.fcv_main).popBackStack(R.id.runningOptionFragment, false)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
