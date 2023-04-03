package com.d204.rumeet.ui.mypage.setting

import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationSettingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationSettingFragment :
    BaseFragment<FragmentNotificationSettingBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification_setting
    override val viewModel: MyPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage) { defaultViewModelProviderFactory }

    override fun initStartView() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.notificationSettingState.collectLatest {
                    val response = it.successOrNull()
                    binding.swSettingNotificationFriend.isChecked = response?.friendAlarm != 0
                    binding.swSettingNotificationRunning.isChecked = response?.matchingAlarm != 0
                }
            }
        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
        binding.swSettingNotificationFriend.setOnCheckedChangeListener { _, isChecked ->
            val target = 0
            val state = if (isChecked) 1 else 0
            viewModel.modifyNotificationState(target, state)
        }

        binding.swSettingNotificationRunning.setOnCheckedChangeListener { _, isChecked ->
            val target = 1
            val state = if(isChecked) 1 else 0
            viewModel.modifyNotificationState(target, state)
        }
    }

    private fun editNotificationState(){

    }

}