package com.d204.rumeet.ui.notification

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.notification.adapter.NotificationContainerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : BaseFragment<FragmentNotificationBinding, NotificationViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification
    override val viewModel: NotificationViewModel by activityViewModels()
    private val args by navArgs<NotificationFragmentArgs>()

    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView(){
        val runningRequest = NotificationContainerFragment(getString(R.string.title_request_running))
        val friendRequest = NotificationContainerFragment(getString(R.string.title_request_friend))
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
        binding.vpNotification.currentItem = args.type

        TabLayoutMediator(binding.tblyNotification, binding.vpNotification){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

}