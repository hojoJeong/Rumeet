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
                    is ChattingSideEffect.RequestSendMessage -> {

                    }
                    is ChattingSideEffect.StartSubscribeRabbiMq -> {

                    }
                    is ChattingSideEffect.SendChatting -> {
                        if(binding.btnChattingSubmit.text.toString().isNotEmpty())
                            AMQPManager.sendMessage(binding.editChattingInput.text.toString())
                    }
                    is ChattingSideEffect.ReceiveChatting -> {
                        val currentChattingList = chattingAdapter.currentList
                        currentChattingList.add(it.messageModel)
                        chattingAdapter.submitList(currentChattingList
                        )
                        Log.d("TAG", "initDataBinding: ${it.messageModel}")
                    }
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