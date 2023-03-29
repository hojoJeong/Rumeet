package com.d204.rumeet.ui.mypage.setting

import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentWithdrawalBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawalFragment : BaseFragment<FragmentWithdrawalBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage)
    override val layoutResourceId: Int
        get() = R.layout.fragment_withdrawal
    override val viewModel: BaseViewModel
        get() = myPageViewModel

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
        with(binding.btnWithdrawal){
            setContent("회원 탈퇴하기")
            setState(false)
            addClickListener{
                navigate(WithdrawalFragmentDirections.actionWithdrawalFragmentToResultWithdrawalFragment())
            }
        }
    }
}