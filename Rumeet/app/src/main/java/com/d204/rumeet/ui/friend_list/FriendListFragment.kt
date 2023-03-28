package com.d204.rumeet.ui.friend_list

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentFriendListBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.friend_list.adapter.FriendListAdapter
import com.d204.rumeet.ui.friend_list.model.FriendListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FriendListFragment : BaseFragment<FragmentFriendListBinding, FriendListViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_friend_list

    override val viewModel: FriendListViewModel by viewModels()
    private lateinit var friendAdapter: FriendListAdapter

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        viewModel.requestFriendList()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.friendListAction.collectLatest {
                when (it) {
                    is FriendListAction.SuccessFriendList -> {
                        settingFriendList(it.listSize)
                    }
                    is FriendListAction.SearchFriend -> {
                        viewModel.getFriendInfo(it.userId)
                    }
                    is FriendListAction.SuccessFriendInfo -> {
                        showFriendInfoDialog(it.friendInfo)
                    }
                    is FriendListAction.SortRunTogetherFriend -> {
                        //Todo request sort
                    }
                    is FriendListAction.SortRecentlyRunFriend -> {
                        //Todo request sort
                    }
                }
            }
        }
    }

    private fun settingFriendList(listSize: Int) {
        with(binding.lyNoResult){
            root.visibility = if (listSize == 0) View.VISIBLE else View.GONE
            tvContentNoResultMessage.text = "친구가 없습니다"
        }
        binding.tvAllFriendContent.text = "${listSize}\n전체친구"
    }

    private fun showFriendInfoDialog(friendData: FriendListModel) {
        val dialog = FriendInfoDialog().apply {
            initFriendInfo(friendData)
            addChattingButtonClickListener { userId ->
                //Todo navigate chatting...
            }
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun initAfterBinding() {
        friendAdapter = FriendListAdapter(viewModel)

        binding.editSearchFriend.setOnEditorActionListener { view, actionId, _ ->
            var handle = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                friendAdapter.filterNickName(view.text.toString())
                handle = true
            }
            handle
        }
    }
}