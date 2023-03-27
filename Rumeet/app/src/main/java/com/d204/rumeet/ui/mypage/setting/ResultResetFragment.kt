package com.d204.rumeet.ui.mypage.setting

import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentResultResetBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MypageViewModel

class ResultResetFragment : BaseFragment<FragmentResultResetBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_result_reset
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        binding.btnResultReset.addClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }
}