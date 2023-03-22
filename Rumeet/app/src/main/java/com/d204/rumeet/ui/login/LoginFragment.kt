package com.d204.rumeet.ui.login

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentLoginBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.components.FilledEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels()

    override fun initStartView() {
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.navigationEvent.collectLatest {
                when (it) {
                    is LoginNavigationAction.EmailLogin -> viewModel.login(
                        binding.editLoginId.inputText,
                        binding.editLoginPassword.inputText,
                        binding.btnLoginAuto.isChecked
                    )
                    else -> Log.d(TAG, "initDataBinding: else")
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.editLoginId.setEditTextType(FilledEditText.FilledEditTextType.ID, "ID")
        binding.editLoginPassword.setEditTextType(
            FilledEditText.FilledEditTextType.PASSWORD,
            "비밀번호"
        )
    }

    companion object {
        private const val TAG = "LoginFragment..."
    }
}