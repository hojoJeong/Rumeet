package com.d204.rumeet.ui.join.addtional_info

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentAddtionalInfoBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.join.JoinViewModel
import kotlinx.coroutines.flow.collectLatest

class AdditionalInfoFragment : BaseFragment<FragmentAddtionalInfoBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_addtional_info

    override val viewModel: JoinViewModel by activityViewModels()

    override fun initStartView() {
        with(binding){
            vm = viewModel
            socialType = viewModel.joinInfo.socialJoinModel != null
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.additionalInfoAction.collectLatest {
                when(it){
                    is AdditionalInfoAction.SocialSignUp -> { viewModel.socialSignUp() }
                    is AdditionalInfoAction.EmailSignUp -> { viewModel.signUp() }
                    is AdditionalInfoAction.ShowBodyStateDialog -> { }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.btnRumeet.setContent("러밋하러 가기")
    }
}