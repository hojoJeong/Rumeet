package com.d204.rumeet.ui.mypage.setting

import androidx.activity.addCallback
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentResultWithdrawalBinding
import com.d204.rumeet.ui.activities.LoginActivity
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MyPageViewModel
import com.d204.rumeet.util.startActivityAfterClearBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultWithdrawalFragment : BaseFragment<FragmentResultWithdrawalBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_result_withdrawal
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        with(binding.btnResultWithdrawal){
            setContent(getString(R.string.content_result_withdrawal_btn))
            addClickListener{
                requireActivity().startActivityAfterClearBackStack(LoginActivity::class.java)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback{
            requireActivity().startActivityAfterClearBackStack(LoginActivity::class.java)
        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }
}