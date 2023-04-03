package com.d204.rumeet.ui.notification

import android.app.Notification
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationContainerBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.notification.adapter.NotificationContainerAdapter
import com.d204.rumeet.ui.notification.adapter.NotificationFriendListAdapter
import com.d204.rumeet.ui.notification.adapter.NotificationRunningListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationContainerFragment(private val viewInfo: String) :
    BaseFragment<FragmentNotificationContainerBinding, NotificationViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification_container
    override val viewModel: NotificationViewModel by activityViewModels()

    override fun initStartView() {
        viewModel.getNotificationList()
        binding.contentNotificationNoResult.tvContentNoResultMessage.text = "받은 요청이 없습니다."
        initView()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView() {
        binding.rvNotification.layoutManager =
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
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.runningRequestList.collect {
                    binding.contentNotificationNoResult.tvContentNoResultMessage.visibility = View.GONE
                    val runningAdapter = NotificationRunningListAdapter().apply {
                        submitList(it.successOrNull())
                    }
                    binding.rvNotification.adapter = runningAdapter
                }
            }
        }
    }

    private fun initFriendRequestView() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.friendRequestList.collect{
                    binding.contentNotificationNoResult.tvContentNoResultMessage.visibility = View.GONE
                    val friendAdapter = NotificationFriendListAdapter().apply {
                        submitList(it.successOrNull())
                    }
                    binding.rvNotification.adapter = friendAdapter
                }
            }
        }
    }
}