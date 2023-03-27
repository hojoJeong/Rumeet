package com.d204.rumeet.ui.friend_list

import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentFriendListBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.friend_list.adapter.FriendListAdapter
import kotlinx.coroutines.flow.collectLatest

class FriendListFragment : BaseFragment<FragmentFriendListBinding, FriendListViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_friend_list

    override val viewModel: FriendListViewModel by viewModels()
    private val friendAdapter = FriendListAdapter(viewModel)

    override fun initStartView() {
        with(binding){
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        viewModel.requestFriendList()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.friendListAction.collectLatest {
                when(it){
                    is FriendListAction.SuccessFriendList -> {
                        binding.tvAllFriendContent.text = "${it.listSize}\n전체친구"
                    }
                    is FriendListAction.SearchFriend -> {
                        viewModel.getFriendInfo(it.userId)
                    }
                    is FriendListAction.ShowFriendInfoDialog -> {
                        //Todo show dialog
                    }
                    is FriendListAction.SortRunTogetherFriend ->{
                        //Todo request sort
                    }
                    is FriendListAction.SortRecentlyRunFriend -> {
                        //Todo request sort
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.editSearchFriend.setOnEditorActionListener { view, actionId, _ ->
            var handle = false
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                friendAdapter.filterNickName(view.text.toString())
                handle = true
            }
            handle
        }
    }
}