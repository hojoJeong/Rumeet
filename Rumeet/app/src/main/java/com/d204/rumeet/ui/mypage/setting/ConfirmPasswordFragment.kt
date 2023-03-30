package com.d204.rumeet.ui.mypage.setting

import android.content.ContentValues.TAG
import android.util.Log
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentConfirmPasswordBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.SingleLineEditText
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPasswordFragment : BaseFragment<FragmentConfirmPasswordBinding, BaseViewModel>() {
    private val args: ConfirmPasswordFragmentArgs by navArgs()
    override val layoutResourceId: Int
        get() = R.layout.fragment_confirm_password
    override val viewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage){defaultViewModelProviderFactory}


    override fun initStartView() {
        binding.btnConfirmPassword.setContent("계속")
        Log.d(TAG, "initStartView: ${args.destinationFragment}")
        with(binding.editConfirmPassword){
            setEditTextType(SingleLineEditText.SingUpEditTextType.PASSWORD, getString(R.string.content_confirm_password))
        }

        binding.btnConfirmPassword.addClickListener {
            Log.d(TAG, "initStartView: ${args.destinationFragment}")

            when(args.destinationFragment){
                "resetPassword" -> {
                    navigate(ConfirmPasswordFragmentDirections.actionConfirmPasswordFragmentToResetPasswordFragment2(viewModel.userInfo.value.successOrNull()!!.email, true))
                }
            }
        }

    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

}