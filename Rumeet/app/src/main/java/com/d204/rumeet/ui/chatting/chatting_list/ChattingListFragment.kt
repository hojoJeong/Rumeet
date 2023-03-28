package com.d204.rumeet.ui.chatting.chatting_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentChattingBinding
import com.d204.rumeet.databinding.FragmentChattingListBinding
import com.d204.rumeet.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChattingListFragment : BaseFragment<FragmentChattingListBinding, ChattingListViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_chatting_list

    override val viewModel: ChattingListViewModel by viewModels()

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        viewModel.requestChattingRoom()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.chattingListSideEffect.collectLatest {
                when (it) {
                    is ChattingListSideEffect.SuccessGetChattingList -> {
                        binding.contentNoResultChattingList.root.visibility =
                            if (it.isEmpty) View.VISIBLE else View.GONE
                    }
                    is ChattingListSideEffect.NavigateChattingRoom -> {
                        navigate(ChattingListFragmentDirections.actionChattingListFragmentToChattingFragment(it.chattingRoomId))
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.contentNoResultChattingList.tvContentNoResultMessage.text = "채팅내역이 존재하지 않습니다"
    }
}