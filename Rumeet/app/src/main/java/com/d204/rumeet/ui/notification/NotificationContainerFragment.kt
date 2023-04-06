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
        initRunningAction()
        binding.rvNotification.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        when (viewInfo) {
            getString(R.string.title_request_running) -> {
                initRunningAdapter()
            }

            getString(R.string.title_request_friend) -> {
                initFriendListAdapter()
            }
        }
    }

    private fun initRunningAction() {
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
                        toastMessage("러닝 요청을 거절하였습니다.")
                    }
                    is NotificationAction.FriendRequest -> {}
                    is NotificationAction.RunningRequest -> {}
                }

            }
        }
    }

    private fun initRunningAdapter() {
        val runningAdapter = NotificationRunningListAdapter().apply {
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

        initRunningRequestView(runningAdapter)
    }

    private fun initRunningRequestView(runningAdapter: NotificationRunningListAdapter) {
        binding.rvNotification.adapter = runningAdapter
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.runningRequestList.collect {
                    viewModel.dismissLoading()
                    val runningRequestList = it.successOrNull() ?: emptyList()
                    if (runningRequestList.isNotEmpty()) {
                        binding.contentNotificationNoResult.root.visibility =
                            View.GONE
                        runningAdapter.submitList(runningRequestList)
                    } else {
                        binding.contentNotificationNoResult.root.visibility =
                            View.VISIBLE
                        runningAdapter.submitList(emptyList())
                    }
                }
            }
        }
    }

    private fun initFriendListAdapter() {
        val friendAdapter = NotificationFriendListAdapter().apply {
            notificationHandler = object : NotificationHandler {
                override fun onClickFriend(
                    friendId: Int,
                    myId: Int,
                    accept: Boolean
                ) {
                    if (accept) {
                        viewModel.acceptRequestFriend(friendId, myId)
                        toastMessage("친구 요청을 수락하였습니다.\n이제 함께 러밋해봐요!")
                    } else {
                        viewModel.denyRequestFriend(friendId, myId)
                        toastMessage("친구 요청을 거절하였습니다.")
                    }
                }

                override fun onClickRunning(
                    raceId: Int,
                    index: Int,
                    accept: Boolean
                ) {

                }
            }
        }
        initFriendRequestView(friendAdapter)
    }

    private fun initFriendRequestView(friendAdapter: NotificationFriendListAdapter) {
        binding.rvNotification.adapter = friendAdapter

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.friendRequestList.collect {
                    viewModel.dismissLoading()
                    val friendRequestList = it.successOrNull() ?: emptyList()
                    if (friendRequestList.isNotEmpty()) {
                        binding.contentNotificationNoResult.root.visibility =
                            View.GONE
                        friendAdapter.submitList(friendRequestList)
                    } else {
                        binding.contentNotificationNoResult.root.visibility = View.VISIBLE
                        friendAdapter.submitList(emptyList())
                    }
                }
            }
        }
    }
}