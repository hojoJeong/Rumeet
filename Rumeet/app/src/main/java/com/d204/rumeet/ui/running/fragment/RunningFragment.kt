package com.d204.rumeet.ui.running.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel

class RunningFragment : BaseFragment<FragmentRunningBinding, BaseViewModel>() {
    private val runningViewModel by activityViewModels<RunningViewModel>()
    override val layoutResourceId: Int
        get() = R.layout.fragment_running
    override val viewModel: BaseViewModel
        get() = runningViewModel

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }


}