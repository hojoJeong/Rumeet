package com.d204.rumeet.ui.mypage.setting

import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationSettingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationSettingFragment : BaseFragment<FragmentNotificationSettingBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage)
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification_setting
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }


}