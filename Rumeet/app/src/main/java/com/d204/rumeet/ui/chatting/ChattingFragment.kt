package com.d204.rumeet.ui.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentChattingBinding
import com.d204.rumeet.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.exp

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_chatting

    override val viewModel: ChattingViewModel by viewModels()

    override fun initStartView() {
        with(binding){
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        viewModel.requestChattingData(3)
        lifecycleScope.launchWhenResumed {
            viewModel.chattingSideEffect.collectLatest {
                when(it){
                    is ChattingSideEffect.RequestSendMessage -> {}
                }
            }
        }
    }

    override fun initAfterBinding() {

    }
}