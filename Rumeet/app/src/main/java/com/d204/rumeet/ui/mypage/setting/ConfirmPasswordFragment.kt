package com.d204.rumeet.ui.mypage.setting

import android.content.ContentValues.TAG
import android.util.Log
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentConfirmPasswordBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.components.SingleLineEditText
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPasswordFragment : BaseFragment<FragmentConfirmPasswordBinding, BaseViewModel>() {
    private val mypageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage)
    private val args: ConfirmPasswordFragmentArgs by navArgs()

    override val layoutResourceId: Int
        get() = R.layout.fragment_confirm_password
    override val viewModel: BaseViewModel
        get() = mypageViewModel

    override fun initStartView() {
        binding.btnConfirmPassword.setContent("계속")
        Log.d(TAG, "initStartView: ${args.destinationFragment}")
        with(binding.editConfirmPassword){
            setEditTextType(SingleLineEditText.SingUpEditTextType.PASSWORD, getString(R.string.content_confirm_password))
        }

        binding.btnConfirmPassword.addClickListener {
            Log.d(TAG, "initStartView: ${args.destinationFragment}")

            when(args.destinationFragment){
                "resetDetailUserInfo" -> {
                    navigate(ConfirmPasswordFragmentDirections.actionConfirmPasswordFragmentToAdditionalInfoFragment2(true))
                }
                "resetPassword" -> {
                    navigate(ConfirmPasswordFragmentDirections.actionConfirmPasswordFragmentToResetPasswordFragment2("", true))
                }
                "withdrawal" -> {
                    navigate(ConfirmPasswordFragmentDirections.actionConfirmPasswordFragmentToWithdrawalFragment())
                }
            }
        }

    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

}