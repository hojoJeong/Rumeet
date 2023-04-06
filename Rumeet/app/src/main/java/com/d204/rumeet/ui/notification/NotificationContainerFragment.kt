package com.d204.rumeet.ui.notification

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentNotificationContainerBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.notification.adapter.NotificationFriendListAdapter
import com.d204.rumeet.ui.notification.adapter.NotificationRunningListAdapter
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationContainerFragment(private val viewInfo: String) :
    BaseFragment<FragmentNotificationContainerBinding, NotificationViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_notification_container
    override val viewModel: NotificationViewModel by activityViewModels()
    private val runningViewModel by activityViewModels<RunningViewModel>()

    override fun initStartView() {
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.getNotificationList()
        binding.contentNotificationNoResult.tvContentNoResultMessage.text = "받은 요청이 없습니다."
        initView()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView() {
        lifecycleScope.launchWhenResumed {
            viewModel.notificationAction.collectLatest { action ->
                when (action) {
                    is NotificationAction.AcceptFriendRequest -> {}
                    is NotificationAction.AcceptRunningRequest -> {
                        val info =
                            viewModel.runningRequestList.value.successOrNull()?.get(action.index)
                        findNavController().navigate(
                            NotificationFragmentDirections.actionNotificationFragmentToNavigationRunning(
                                invitedFromFriend = true,
                                myId = viewModel.userId,
                                roomId = action.raceId,
                                gameType = info?.mode ?: -1,
                                partnerId = info?.partnerId ?: -1
                            )
                        )
                        Log.d(TAG, "initView: accept!!! ${action.raceId}, $info")

                    }
                    is NotificationAction.DenyFriendRequest -> {}
                    is NotificationAction.DenyRunningRequest -> {
                        Log.d(TAG, "initView: deny ${action.raceId}")
                    }
                    is NotificationAction.FriendRequest -> {}
                    is NotificationAction.RunningRequest -> {}
                }

            }
        }
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
                    if (it.successOrNull()?.size ?: 0 > 0) {
                        binding.contentNotificationNoResult.root.visibility =
                            View.GONE
                        val runningAdapter = NotificationRunningListAdapter().apply {
                            submitList(it.successOrNull() ?: emptyList())
                            handler = object : NotificationHandler {
                                override fun onClickFriend(
                                    friendId: Int,
                                    myId: Int,
                                    accept: Boolean
                                ) {
                                }

                                override fun onClickRunning(
                                    index: Int,
                                    raceId: Int,
                                    accept: Boolean
                                ) {
                                    if (accept) {
                                        viewModel.acceptRequestRunning(raceId, index)
                                    } else {
                                        viewModel.denyRequestRunning(raceId, index)
                                    }
                                }

                            }
                        }
                        binding.rvNotification.adapter = runningAdapter
                    } else {
                        binding.contentNotificationNoResult.root.visibility =
                            View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initFriendRequestView() {
        val friendAdapter = NotificationFriendListAdapter().apply {
            notificationHandler = object : NotificationHandler {
                override fun onClickFriend(
                    friendId: Int,
                    myId: Int,
                    accept: Boolean
                ) {
                    if (accept) {
                        viewModel.acceptRequestFriend(friendId, myId)
                    } else {
                        viewModel.denyRequestFriend(friendId, myId)
                    }
                    viewModel.getNotificationList()
                }

                override fun onClickRunning(
                    raceId: Int,
                    index: Int,
                    accept: Boolean
                ) {

                }
            }
        }
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.friendRequestList.collect {
                    if (it.successOrNull()?.size ?: 0 > 0) {
                        binding.contentNotificationNoResult.root.visibility =
                            View.GONE
                        friendAdapter.submitList(it.successOrNull())
                        binding.rvNotification.adapter = friendAdapter
                    } else {
                        binding.contentNotificationNoResult.root.visibility = View.GONE
                        friendAdapter.submitList(emptyList())
                    }

                }
            }
        }
    }
}
