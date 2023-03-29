package com.d204.rumeet.ui.chatting

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentChattingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.chatting.adapter.ChattingItemAdapter
import com.d204.rumeet.util.AMQPManager
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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
                    is ChattingSideEffect.SuccessChattingData -> {
                        chattingAdapter = ChattingItemAdapter(2, args.profile)
                        binding.rvChattingContent.adapter = chattingAdapter
                    }
                    is ChattingSideEffect.SendChatting -> {
                        if(binding.editChattingInput.text.toString().isNotEmpty()){
                            AMQPManager.sendMessage(binding.editChattingInput.text.toString())
                            binding.editChattingInput.setText("")
                        }
                    }
                    is ChattingSideEffect.ReceiveChatting -> {
                       toastMessage("됐따!!")
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        viewModel.requestChattingData(args.chattingRoomId)
    }
}