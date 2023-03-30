package com.d204.rumeet.ui.running.matching

import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningMatchingFailBinding
import com.d204.rumeet.ui.base.BaseFragment

class RunningMatchingFailFragment : BaseFragment<FragmentRunningMatchingFailBinding, RunningMatchingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_matching_fail

    override val viewModel: RunningMatchingViewModel by viewModels()

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.contentNoResult.tvContentNoResultMessage.text = "매칭에 실패하였습니다"
        binding.btnOkay.setOnClickListener {
            navigate(RunningMatchingFailFragmentDirections.actionRunningMatchingFailFragmentToRunningOptionFragment())
        }
    }
}