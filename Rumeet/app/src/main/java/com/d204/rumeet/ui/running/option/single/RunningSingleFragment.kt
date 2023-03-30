package com.d204.rumeet.ui.running.option.single

import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningSingleBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningSingleFragment : BaseFragment<FragmentRunningSingleBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_single

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}