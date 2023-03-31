package com.d204.rumeet.ui.notification

import android.app.Notification
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationContainerBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.notification.adapter.NotificationContainerAdapter
import com.d204.rumeet.ui.notification.adapter.NotificationFriendListAdapter
import com.d204.rumeet.ui.notification.adapter.NotificationRunningListAdapter

class NotificationContainerFragment(private val viewInfo: String) :
    BaseFragment<FragmentNotificationContainerBinding, BaseViewModel>() {
    private val notificationViewModel by activityViewModels<NotificationViewModel>()
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification_container
    override val viewModel: BaseViewModel
        get() = notificationViewModel

    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView() {
        binding.rvNotificationFriend.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        when (viewInfo) {
            getString(R.string.title_request_running) -> {
                initRunningRequestView()
            }

            getString(R.string.title_request_friend) -> {
                initFriendRequestView()
            }
        }
    }

    private fun initRunningRequestView() {
        val runningAdapter = NotificationRunningListAdapter().apply {

        }
    }

    private fun initFriendRequestView() {
        val friendAdapter = NotificationFriendListAdapter().apply {

        }
    }
}