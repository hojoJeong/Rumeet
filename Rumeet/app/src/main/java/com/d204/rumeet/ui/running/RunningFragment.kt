package com.d204.rumeet.ui.running

import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.util.amqp.RunningAMQPManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding, RunningViewModel>() {

    private val runningViewModel by navGraphViewModels<RunningViewModel>(R.id.navigation_running)

    override val layoutResourceId: Int get() = R.layout.fragment_running
    override val viewModel: RunningViewModel get() = runningViewModel

    //userid, partnerId, gameType
    private val args by navArgs<RunningFragmentArgs>()

    override fun initStartView() {
        Log.d(TAG, "initStartView: ${args.myId}, ${args.partnerId}, ${args.gameType}, ${args.roomId}")
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        RunningAMQPManager.sendRunning()
        RunningAMQPManager.receiveRunning()
    }
}

private const val TAG = "RunningFragment"