package com.d204.rumeet.ui.running.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningOptionRelayBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel

class RunningOptionRelayFragment : BaseFragment<FragmentRunningOptionRelayBinding, BaseViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_option_relay
    override val viewModel: BaseViewModel
        get() = RunningViewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

}