package com.d204.rumeet.ui.chatting.chatting_list

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentChattingListBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.chatting.chatting_list.adapter.ChattingListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ChattingListFragment"

@AndroidEntryPoint
class ChattingListFragment : BaseFragment<FragmentChattingListBinding, ChattingListViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_chatting_list

    override val viewModel: ChattingListViewModel by viewModels()
    private lateinit var chattingListAdapter: ChattingListAdapter
    private var flag = false

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
        viewModel.requestChattingRoom()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenStarted {
            viewModel.chattingListSideEffect.collectLatest {
                when (it) {
                    is ChattingListSideEffect.SuccessGetChattingList -> {
                        setNoResult(it.isEmpty)
                    }
                    is ChattingListSideEffect.NavigateChattingRoom -> {
                        Log.d(TAG, "chatting: ${it.otherUserId}")
                        navigate(
                            ChattingListFragmentDirections.actionChattingListFragmentToChattingFragment(
                                profile = it.profile,
                                chattingRoomId = it.chattingRoomId,
                                noReadCnt = it.noReadCnt,
                                otherUserId = it.otherUserId
                            )
                        )
                    }
                    is ChattingListSideEffect.SuccessNewChattingList -> {
                        if (it.chattingRoomInfo.isEmpty()) {
                            binding.contentNoResultChattingList.root.visibility = View.VISIBLE
                        } else {
                            binding.contentNoResultChattingList.root.visibility = View.GONE
                            binding.rvChattingRoom.visibility = View.INVISIBLE
                            chattingListAdapter.submitList(null)
                            chattingListAdapter.submitList(it.chattingRoomInfo.toList())
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.rvChattingRoom.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setNoResult(isEmpty: Boolean) {
        Log.d(TAG, "setNoResult: $isEmpty")
        if (isEmpty) {
            binding.contentNoResultChattingList.root.visibility = View.VISIBLE
            binding.contentNoResultChattingList.tvContentNoResultMessage.text = "채팅 내역이 없습니다"
        }
    }

    override fun initAfterBinding() {
        chattingListAdapter = ChattingListAdapter(viewModel)
        binding.rvChattingRoom.adapter = chattingListAdapter
//        binding.rvChattingRoom.itemAnimator = null

        binding.contentNoResultChattingList.tvContentNoResultMessage.text = "채팅내역이 존재하지 않습니다"
    }

}