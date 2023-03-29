package com.d204.rumeet.ui.chatting

import android.util.JsonReader
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentChattingBinding
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.chatting.adapter.ChattingItemAdapter
import com.d204.rumeet.ui.chatting.model.ChattingMessageUiModel
import com.d204.rumeet.util.AMQPManager
import com.d204.rumeet.util.scrollToBottom
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.logging.Handler

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_chatting

    override val viewModel: ChattingViewModel by viewModels()
    private val args by navArgs<ChattingFragmentArgs>()
    private lateinit var chattingAdapter: ChattingItemAdapter

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.chattingSideEffect.collectLatest {
                when (it) {
                    is ChattingSideEffect.SuccessChattingData -> {
                        chattingAdapter = ChattingItemAdapter(it.userId, args.profile)
                        binding.rvChattingContent.adapter = chattingAdapter
                    }
                    is ChattingSideEffect.SendChatting -> {
                        if (binding.editChattingInput.text.toString().isNotEmpty()) {
                            launch {
                                AMQPManager.sendMessage(
                                    Gson().toJson(
                                        ChattingMessageModel(
                                            roomId = args.chattingRoomId,
                                            toUserId = viewModel.chattingUserId.value,
                                            fromUserId = viewModel.userId.value,
                                            binding.editChattingInput.text.toString(),
                                            System.currentTimeMillis()
                                        )
                                    )
                                )
                            }
                        }
                    }
                    is ChattingSideEffect.ReceiveChatting -> {
                        if (it.messageModel != null) {
                            binding.editChattingInput.setText("")
                            val list = chattingAdapter.currentList.toMutableList()
                            list.add(
                                ChattingMessageUiModel(
                                    it.messageModel,
                                    list[list.size - 1].message.fromUserId != viewModel.userId.value
                                )
                            )
                            chattingAdapter.submitList(list)
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(300)
                                binding.rvChattingContent.scrollToBottom()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        viewModel.requestChattingData(args.chattingRoomId)
        binding.rvChattingContent.itemAnimator = null

        val event = View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if(bottom < oldBottom) binding.rvChattingContent.scrollBy(0, oldBottom - bottom)
        }
        binding.rvChattingContent.addOnLayoutChangeListener(event)
    }
}