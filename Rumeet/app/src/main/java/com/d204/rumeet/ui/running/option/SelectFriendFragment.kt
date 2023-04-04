package com.d204.rumeet.ui.running.option

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSelectFriendBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.friend.list.FriendListViewModel
import com.d204.rumeet.ui.home.HomeHandler
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.ui.running.matching.RunningMatchingViewModel
import com.d204.rumeet.ui.running.option.adapter.SelectFriendListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SelectFriendFragment : BaseFragment<FragmentSelectFriendBinding, RunningMatchingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_select_friend
    override val viewModel: RunningMatchingViewModel by activityViewModels<RunningMatchingViewModel>()
    private val friendViewModel by activityViewModels<FriendListViewModel>()
    private val args by navArgs<SelectFriendFragmentArgs>()
    override fun initStartView() {
        friendViewModel.requestFriendList(1)
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            launch {
                friendViewModel.friendList.collectLatest {
                    val friendList = it.successOrNull()
                    val friendAdapter = SelectFriendListAdapter().apply {
                        submitList(friendList)
                        handler = object : HomeHandler{
                            override fun onClick(userId: Int) {
                                Log.d(TAG, "onClick: selectFragment 러닝 시작 userId: $userId")
                                viewModel.setFriendId(userId)
                                navigate(SelectFriendFragmentDirections.actionSelectFriendFragmentToRunningMatchingFragment(args.gameType, true))
                            }
                        }
                    }

                    with(binding.rvSelectFriend){
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        adapter = friendAdapter
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

}