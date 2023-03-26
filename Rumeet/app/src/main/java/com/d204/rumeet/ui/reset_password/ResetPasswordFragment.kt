package com.d204.rumeet.ui.reset_password

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentResetPasswordBinding
import com.d204.rumeet.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>(){
    override val layoutResourceId: Int
        get() =  R.layout.fragment_reset_password

    override val viewModel: ResetPasswordViewModel by viewModels()

    override fun initStartView() {
        with(binding){
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        viewModel.resetPasswordAction.collectLatest {
            when(it){
                is ResetPasswordAction.SuccessResetPassword -> {
                    viewModel
                }
                is ResetPasswordAction.RequestResetPassword -> {

                }
            }
        }
    }

    override fun initAfterBinding() {

    }
}