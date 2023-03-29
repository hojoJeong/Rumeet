package com.d204.rumeet.ui.mypage.setting

import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentWithdrawalBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WithdrawalFragment : BaseFragment<FragmentWithdrawalBinding, BaseViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_withdrawal
    override val viewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage) { defaultViewModelProviderFactory }

    override fun initStartView() {
        initWithdrawalBtn()
        initReasonMenu()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initReasonMenu() {
        val reasons = resources.getStringArray(R.array.content_withdrawal_reason)
        val menuAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, reasons)
        val menu = binding.menuWithdrawalReasonItem.apply {
            setAdapter(menuAdapter)
        }

        menu.setOnItemClickListener { _, _, _, _ ->
            menu.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnWithdrawal.setState(true)
            binding.btnWithdrawal.setBtnBackGround(R.drawable.bg_rect_transparent_red_round8_stroke0)
        }
    }

    private fun initWithdrawalBtn() {
        with(binding.btnWithdrawal) {
            setContent("회원 탈퇴하기")
            setState(false)
            addClickListener {
                viewModel.withdrawal()
                checkWithdrawalResult()
            }
        }
    }

    private fun checkWithdrawalResult() {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.resultWithdrawal.collect {
                    val result = viewModel.resultWithdrawal.value.successOrNull() ?: false
                    if (result) {
                        navigate(WithdrawalFragmentDirections.actionWithdrawalFragmentToResultWithdrawalFragment())
                    } else {
                        toastMessage("회원탈퇴에 실패하였습니다. 다시 시도해주세요.")
                    }
                }
            }
        }
    }
}