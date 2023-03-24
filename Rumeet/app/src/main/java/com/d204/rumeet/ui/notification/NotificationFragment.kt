package com.d204.rumeet.ui.notification

import androidx.fragment.app.activityViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.notification.adapter.NotificationContainerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : BaseFragment<FragmentNotificationBinding, BaseViewModel>() {
    private val notificationViewModel by activityViewModels<NotificationViewModel>()
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification
    override val viewModel: BaseViewModel
        get() = notificationViewModel

    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView(){
        val runningRequest = NotificationContainerFragment().apply {
            setViewInfo(getString(R.string.title_request_running))
        }
        val friendRequest = NotificationContainerFragment().apply {
            setViewInfo(getString(R.string.title_request_friend))
        }

        val vpFragmentList = listOf(
            runningRequest,
            friendRequest
        )

        val tabList = listOf(
            getString(R.string.title_request_running),
            getString(R.string.title_request_friend)
        )

        binding.vpNotification.adapter = NotificationContainerAdapter(this).apply {
            setFragmentList(vpFragmentList)
        }

        TabLayoutMediator(binding.tblyNotification, binding.vpNotification){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

}