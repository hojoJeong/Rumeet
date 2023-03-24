package com.d204.rumeet.ui.join.password

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinPasswordBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.components.SingUpEditText
import com.d204.rumeet.ui.join.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class JoinPasswordFragment : BaseFragment<FragmentJoinPasswordBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_password

    override val viewModel: JoinViewModel by activityViewModels()

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.joinPasswordAction.collectLatest {
                when (it) {
                    is JoinPasswordAction.CheckPasswordValidation -> {
                        if (binding.editPassword.passwordValidate && binding.editPasswordCheck.checkPasswordMatch(binding.editPassword.keyword)) {
                            viewModel.joinInfo.password = binding.editPassword.keyword
                            navigate(JoinPasswordFragmentDirections.actionJoinPasswordFragmentToAdditionalInfoFragment())
                        }
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.editPassword.setEditTextType(SingUpEditText.SingUpEditTextType.PASSWORD)
        binding.editPasswordCheck.setEditTextType(SingUpEditText.SingUpEditTextType.PASSWORD)
        binding.btnSignUp.setContent("계속하기")
    }
}