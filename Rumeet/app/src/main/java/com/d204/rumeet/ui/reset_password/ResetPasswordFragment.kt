package com.d204.rumeet.ui.reset_password

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentResetPasswordBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.SingleLineEditText
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>(){
    override val layoutResourceId: Int
        get() =  R.layout.fragment_reset_password

    override val viewModel: ResetPasswordViewModel by viewModels()
    private val args by navArgs<ResetPasswordFragmentArgs>()
    private val myPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage){defaultViewModelProviderFactory}

    override fun initStartView() {
        with(binding){
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.resetPasswordAction.collectLatest {
                when(it){
                    is ResetPasswordAction.SuccessResetPassword -> {
                        if(!args.reset){
                            navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment())
                        } else {
                            findNavController().popBackStack()
                        }
                    }
                    is ResetPasswordAction.RequestResetPassword -> {
                        if (binding.editPassword.passwordValidate && binding.editPasswordCheck.checkPasswordMatch(binding.editPassword.keyword)) {
                            if(!args.reset){
                                //TODO 이메일 인증 시 가입된 회원인지 확인하고 가입된 회원이면 회원 id를 반환 받고 파라미터로 id 담아서 줘야함
//                                viewModel.requestResetPassword(args.email, binding.editPassword.keyword)
                            } else {
                                viewModel.requestResetPassword(myPageViewModel.userId.value.successOrNull()!!, myPageViewModel.userInfo.value.successOrNull()!!.email, binding.editPassword.keyword)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.editPassword.setEditTextType(SingleLineEditText.SingUpEditTextType.PASSWORD, getString(R.string.content_password_hint))
        binding.editPasswordCheck.setEditTextType(SingleLineEditText.SingUpEditTextType.PASSWORD, getString(R.string.content_password_check_hint))
        binding.btnResetPassword.setContent("비밀번호 재설정")
    }
}