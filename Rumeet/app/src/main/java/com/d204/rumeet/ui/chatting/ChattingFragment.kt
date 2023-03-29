package com.d204.rumeet.ui.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentChattingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.chatting.adapter.ChattingItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.exp

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_chatting

    override val viewModel: ChattingViewModel by viewModels()
    private val args by navArgs<ChattingFragmentArgs>()
    private lateinit var chattingAdapter : ChattingItemAdapter

    override fun initStartView() {
        with(binding){
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.chattingSideEffect.collectLatest {
                when(it){
                    is ChattingSideEffect.RequestSendMessage -> {}
                }
            }
        }
    }

    override fun initAfterBinding() {
        viewModel.requestChattingData(args.chattingRoomId)
        chattingAdapter = ChattingItemAdapter(args.chattingRoomId, args.profile)

        binding.rvChattingContent.adapter = chattingAdapter
    }
}