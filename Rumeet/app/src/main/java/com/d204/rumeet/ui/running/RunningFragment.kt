package com.d204.rumeet.ui.running

import android.content.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningBinding
import com.d204.rumeet.service.RunningService
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.model.RunningModel1pace
import com.d204.rumeet.ui.running.model.RunningModel2pace
import com.d204.rumeet.ui.running.model.RunningModel3pace
import com.d204.rumeet.ui.running.model.RunningModel5pace
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.d204.rumeet.util.floatTo2f
import com.d204.rumeet.util.getCalorie
import com.d204.rumeet.util.toMinute
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding, RunningViewModel>(), SensorEventListener {

    override val layoutResourceId: Int get() = R.layout.fragment_running
    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running) {defaultViewModelProviderFactory}

    private lateinit var sensorManager : SensorManager
    private lateinit var altitudeSensor : Sensor

    private val args by navArgs<RunningFragmentArgs>()
    private var bindState = false

    private lateinit var runningService: RunningService
    private val locationList = mutableListOf<Location>()
    private var time = 0L
    private var maxDistance = 0
    private lateinit var runningEndModel: Any
    private var checkCount = 0
    private var pace1 = 0
    private var pace2 = 0
    private var pace3 = 0
    private var pace5 = 0

    private var pace1Flag = false
    private var pace2Flag = false
    private var pace3Flag = false
    private var pace5Flag = false

    private var gender = -1
    private var weight = 0f
    private var age = 0

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
            val runningDistance = intent?.getFloatExtra("distance", 0f)?.div(1000f) ?: 0f
            val runningLocation = intent?.getParcelableExtra<Location>("location")

            // km으로 변환 10.234134 -> 0.01
            binding.tvRunningDistance.text = floatTo2f(runningDistance)
            locationList.add(runningLocation ?: throw IllegalAccessException("NO LOCATION"))

            // km로 변환된 값을 통해 속력을 구함
            val kmPerHour = runningDistance.times(1000).div(time.div(1000).times(3600))
            toastMessage("속력 $kmPerHour")

            binding.tvRunningPace.text = floatTo2f(kmPerHour)
            binding.tvRunningCalorie.text = getCalorie(gender, age, weight, time)

            // 페이스 기록
            if(runningDistance >= 1f && !pace1Flag){
                pace1 = time.div(1000).toInt()
                pace1Flag = true
            } else if(runningDistance >= 2 && !pace2Flag){
                pace2 = time.div(1000).toInt()
                pace2Flag = true
            } else if(runningDistance >= 3 && !pace3Flag){
                pace2 = time.div(1000).toInt()
                pace2Flag = true
            }


            // 메시지 전달(파트너에게 나의 정보를 전송)
//            RunningAMQPManager.sendRunning(args.partnerId, args.roomId, runningDistance.toInt().toString())
            RunningAMQPManager.sendRunning(27, args.roomId, runningDistance.toInt().toString())

            //  러닝 종료
            if (maxDistance <= runningDistance) {

                // 5인경우 기록
                if(!pace5Flag) pace5 = time.div(1000).toInt()

                // 담을 객체 전송
                val message = when (maxDistance) {
                    1000 -> {
                        val response = runningEndModel as RunningModel1pace
                        response.pace1 = pace1
                        Gson().toJson(response)
                    }
                    2000 -> {
                        val response = runningEndModel as RunningModel2pace
                        response.pace1 = pace1
                        response.pace2 = pace2
                        Gson().toJson(response)
                    }
                    3000 -> {
                        val response = runningEndModel as RunningModel3pace
                        response.pace1 = pace1
                        response.pace2 = pace2
                        response.pace3 = pace3
                        Gson().toJson(response)
                    }
                    5000 -> {
                        val response = runningEndModel as RunningModel5pace
                        response.pace1 = pace1
                        response.pace2 = pace2
                        response.pace3 = pace3
                        response.pace5 = pace5
                        Gson().toJson(response)
                    }
                    else -> throw java.lang.IllegalArgumentException("NO MAX DISTANCE")
                }
                // 게임 종료 보내기
                RunningAMQPManager.sendEndGame(message)
                // Todo ViewModel에 전송
                // Todo navigate
                toastMessage("end game!")
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val timer = object : Runnable {
        override fun run() {
            time += 1000
            binding.tvRunningTime.text = time.toMinute()
            handler.postDelayed(this, 1000)
        }
    }

    override fun initStartView() {
        viewModel.getUserInfo(args.myId)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        altitudeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        initRunningMode()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.runningSideEffect.collectLatest {
                when (it) {
                    is RunningSideEffect.SuccessRunning -> {
                        // progress
                        toastMessage("상대방 러닝 : ${it.distance}")
                    }
                    is RunningSideEffect.EndRunning -> {

                    }

                    is RunningSideEffect.SuccessUserInfo -> {
                        //viewModel.startRun(args.myId, args.roomId)
                        viewModel.startRun(2, args.roomId)
                        Log.d("TAG", "SuccessUserInfo: start")

                        gender = it.userInfo.gender
                        age = it.userInfo.age
                        weight = it.userInfo.weight.toFloat()

                        if(!bindState){
                            Log.d("bindState", "SuccessUserInfo: start")
                            val testIntent = Intent(activity, RunningService::class.java)
                            requireActivity().bindService(testIntent, serviceConnection, Context.BIND_AUTO_CREATE)
                        } else{
                            Log.d("bindState", "initDataBinding: already start")
                        }
                    }
                }
            }
        }
    }

    private fun initRunningMode() {
        binding.tvRunningMode.text = when (args.gameType) {
            0 -> {
                runningEndModel = RunningModel1pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "싱글 1km"
            }
            1 -> {
                runningEndModel = RunningModel2pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "싱글 2km"
            }
            2 -> {
                runningEndModel = RunningModel3pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "싱글 3km"
            }
            3 -> {
                runningEndModel = RunningModel5pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                "싱글 5km"
            }
            4 -> {
                runningEndModel = RunningModel1pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "경쟁 1km"
            }
            5 -> {
                runningEndModel = RunningModel2pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "경쟁 2km"
            }
            6 -> {
                runningEndModel = RunningModel3pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "경쟁 3km"
            }
            7 -> {
                runningEndModel = RunningModel5pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                "경쟁 5km"
            }
            8 -> {
                runningEndModel = RunningModel1pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "협동 1km"
            }
            9 -> {
                runningEndModel = RunningModel2pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "협동 2km"
            }
            10 -> {
                runningEndModel = RunningModel3pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "협동 3km"
            }
            11 -> {
                runningEndModel = RunningModel5pace(
                    userId = args.myId,
                    race_Id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                "협동 5km"
            }
            else -> "오류입니다"
        }
    }

    override fun initAfterBinding() {
        handler.postDelayed(timer, 1000)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter("custom-event"))
        sensorManager.registerListener(this,altitudeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        sensorManager.unregisterListener(this);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(timer)
        activity?.unbindService(serviceConnection)
        requireActivity().unbindService(serviceConnection)
    }

    override fun onSensorChanged(p0: SensorEvent) {
        if(p0.sensor.type == Sensor.TYPE_PRESSURE){
            val altitude = SensorManager.getAltitude(
                SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                p0.values[0]
            )
            toastMessage("고도 $altitude" )
            binding.tvRunningHeight.text = "${altitude}m"
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}