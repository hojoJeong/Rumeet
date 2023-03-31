package com.d204.rumeet.ui.running

import android.content.*
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningBinding
import com.d204.rumeet.service.RunningService
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.util.floatTo2f
import com.d204.rumeet.util.getLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding, RunningViewModel>() {

    private val runningViewModel by navGraphViewModels<RunningViewModel>(R.id.navigation_running)

    override val layoutResourceId: Int get() = R.layout.fragment_running
    override val viewModel: RunningViewModel get() = runningViewModel

    private val args by navArgs<RunningFragmentArgs>()
    private var bindState = false

    private lateinit var runningService: RunningService
    private val locationList = mutableListOf<Location>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RunningService.RunningBinder
            runningService = binder.getService()
            bindState = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bindState = false
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // 10m마다 값을 불러온다. 나오는 값은 10.123123123...
            val runningDistance = intent?.getFloatExtra("distance", 0f) ?: 0f
            val runningLocation = intent?.getParcelableExtra<Location>("location")

            toastMessage("distance : $runningDistance")
            // km으로 변환 10.234134 -> 0.01
            binding.tvRunningDistance.text = "${floatTo2f(runningDistance / 1000)}km"
            locationList.add(runningLocation ?: throw IllegalAccessException("NO LOCATION"))
        }
    }

    override fun initStartView() {
        Log.d(
            TAG,
            "initStartView: ${args.myId}, ${args.partnerId}, ${args.gameType}, ${args.roomId}"
        )
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.btnRunningPause.setOnClickListener {
            Log.d(TAG, "initAfterBinding: ${runningService.getDistance()}")
        }
    }

    override fun onResume() {
        super.onResume()
        val testIntent = Intent(activity, RunningService::class.java)
        requireActivity().bindService(testIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter("custom-event"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unbindService(serviceConnection)
        requireActivity().unbindService(serviceConnection)
    }
}

private const val TAG = "RunningFragment"