package com.d204.rumeet.ui.running

import android.content.*
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningBinding
import com.d204.rumeet.service.RunningService
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.running.finish.model.RunningFinishModel
import com.d204.rumeet.ui.running.model.RunningModel1pace
import com.d204.rumeet.ui.running.model.RunningModel2pace
import com.d204.rumeet.ui.running.model.RunningModel3pace
import com.d204.rumeet.ui.running.model.RunningModel5pace
import com.d204.rumeet.util.*
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.flow.collectLatest


private const val TAG = "RunningFragment"

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding, RunningViewModel>(),
    SensorEventListener {

    override val layoutResourceId: Int get() = R.layout.fragment_running
    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running) { defaultViewModelProviderFactory }

    private lateinit var sensorManager: SensorManager
    private lateinit var altitudeSensor: Sensor

    private val args by navArgs<RunningFragmentArgs>()
    private var bindState = false

    private lateinit var runningService: RunningService
    private val locationList = ArrayList<Location>()
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

    private var sensorTime = 0
    private var currentHeight = 0f

    private var kmPerHour: Float = 0f
    private var currentCalorie = 0f
    private var printHeight = 0f

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

            binding.tvRunningDistance.text = floatTo2f(roundDigit(runningDistance.div(1000f).toDouble(),2).toFloat())
            locationList.add(runningLocation ?: throw IllegalAccessException("NO LOCATION"))

            // 나의 seekbar 진행률을 올린다
            binding.sbMyProgress.progress = runningDistance.toInt()
            Log.d(TAG, "onReceive: my max progress : ${binding.sbMyProgress.max}")
            Log.d(TAG, "onReceive: currnet my distance : ${runningDistance.toInt()}")
            Log.d(TAG, "onReceive: max / current ${binding.sbMyProgress.max} / ${binding.sbMyProgress.progress}")

            // km를 시간으로 나눔
            kmPerHour = runningLocation.speed * 3.6f
            currentCalorie += getCalorie(gender, age, weight, time).toFloat()

            Log.d(TAG, "onReceive: kmPerHour $kmPerHour")
            Log.d(TAG, "onReceive: calorie $currentCalorie")

            binding.tvRunningPace.text = floatTo2f(kmPerHour)
            binding.tvRunningCalorie.text = floatTo2f(currentCalorie)

            // 페이스 기록
            if (runningDistance >= 50f && !pace1Flag) {
                pace1 = time.div(1000).toInt()
                pace1Flag = true
                Log.d(TAG, "onReceive: running 1000 finish")
            } else if (runningDistance >= 2000f && !pace2Flag) {
                pace2 = time.div(1000).toInt()
                pace2Flag = true
                Log.d(TAG, "onReceive: running 2000 finish")
            } else if (runningDistance >= 3000f && !pace3Flag) {
                pace2 = time.div(1000).toInt()
                pace2Flag = true
                Log.d(TAG, "onReceive: running 3000 finish")
            }

            // 메시지 전달(파트너에게 나의 정보를 전송)
            RunningAMQPManager.sendRunning(
                args.partnerId,
                args.roomId,
                runningDistance.toInt().toString()
            )

            //  러닝 종료
            if (maxDistance <= runningDistance) {
                Log.d(TAG, "onReceive: running 5000 end")
                // 5인경우 기록
                if (!pace5Flag) pace5 = time.div(1000).toInt()

                // 게임 종료 보내기
                RunningAMQPManager.sendEndGame(getMessageForEndQueue())
                Log.d(TAG, "onReceive: send end game -> navigate ${getMessageForEndQueue()}")

                // 게임 결과는 러닝 결과에서 api 호출할 것
                navigate(
                    RunningFragmentDirections.actionRunningFragmentToRunningFinishFragment(
                        locationList = locationList.toTypedArray(),
                        RunningFinishModel(
                            success = 1,
                            velocity = kmPerHour,
                            calorie = currentCalorie,
                            height = printHeight,
                            userId = args.myId,
                            raceId = args.roomId,
                            mode = args.gameType,
                            time = time
                        )
                    )
                )
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

    private fun getMessageForEndQueue() : String{
        val message = when (maxDistance) {
            50 -> {
                Log.d(TAG, "onReceive: make 1000 response")
                val response = runningEndModel as RunningModel1pace
                response.user_id = args.myId
                response.race_id = args.roomId
                response.pace1 = pace1
                Gson().toJson(response)
            }
            2000 -> {
                Log.d(TAG, "onReceive: make 2000 response")
                val response = runningEndModel as RunningModel2pace
                response.user_id = args.myId
                response.race_id = args.roomId
                response.pace1 = pace1
                response.pace2 = pace2
                Gson().toJson(response)
            }
            3000 -> {
                Log.d(TAG, "onReceive: make 3000 response")
                val response = runningEndModel as RunningModel3pace
                response.user_id = args.myId
                response.race_id = args.roomId
                response.pace1 = pace1
                response.pace2 = pace2
                response.pace3 = pace3
                Gson().toJson(response)
            }
            5000 -> {
                Log.d(TAG, "onReceive: make 5000 response")
                val response = runningEndModel as RunningModel5pace
                response.user_id = args.myId
                response.race_id = args.roomId
                response.pace1 = pace1
                response.pace2 = pace2
                response.pace3 = pace3
                response.pace5 = pace5
                Gson().toJson(response)
            }
            else -> throw java.lang.IllegalArgumentException("NO MAX DISTANCE")
        }
        Log.d(TAG, "getMessageForEndQueue: $message")
        return message
    }

    override fun initStartView() {
        viewModel.getUserInfo(args.myId)
        viewModel.getPartnerInfo(args.partnerId)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        altitudeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        initRunningMode()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.runningSideEffect.collectLatest {
                when (it) {
                    is RunningSideEffect.SuccessRunning -> {
                        Log.d(TAG, "initDataBinding: partner running : ${it.distance}")
                        binding.sbPartnerProgress.progress = it.distance

                        // 상대방의 거리를 받아 더 커진다면?
                        if (it.distance >= maxDistance) {
                            // end queue에 메시지
                            Log.d(TAG, "initDataBinding: end game you lose ${getMessageForEndQueue()}")

                            // 게임 종료 보내기
                            RunningAMQPManager.sendEndGame(getMessageForEndQueue())

                            navigate(RunningFragmentDirections.actionRunningFragmentToRunningFinishFragment(
                                locationList.toTypedArray(),
                                RunningFinishModel(
                                    success = 0,
                                    velocity = kmPerHour,
                                    calorie = currentCalorie,
                                    height = printHeight,
                                    userId = args.myId,
                                    raceId = args.roomId,
                                    mode = args.gameType,
                                    time = time
                                )
                            ))
                        }
                    }
                    is RunningSideEffect.EndRunning -> {

                    }

                    is RunningSideEffect.SuccessPartnerInfo -> {
                        Glide.with(requireContext())
                            .load(it.partnerInfo.profile)
                            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(999)))
                            .circleCrop()
                            .transform(CenterCrop(), RoundedCornersTransformation(2, 0))
                            .override(100,100)
                            .into(object : CustomTarget<Drawable>(){
                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?
                                ) {
                                    binding.sbPartnerProgress.thumb = resource
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {

                                }
                            })
                    }

                    is RunningSideEffect.SuccessUserInfo -> {
                        // sb의 thumb 설정
                        Glide.with(requireContext())
                            .load(it.userInfo.profile)
                            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(999)))
                            .circleCrop()
                            .override(100,100)
                            .into(object : CustomTarget<Drawable>(){
                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?
                                ) {
                                    binding.sbMyProgress.thumb = resource // Thumb 이미지를 설정합니다.
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {

                                }
                            })

                        viewModel.startRun(args.myId, args.roomId)
                        Log.d("TAG", "SuccessUserInfo: start")

                        gender = it.userInfo.gender
                        age = it.userInfo.age
                        weight = it.userInfo.weight.toFloat()

                        if (!bindState) {
                            Log.d("bindState", "SuccessUserInfo: start")
                            val testIntent = Intent(activity, RunningService::class.java)
                            requireActivity().bindService(
                                testIntent,
                                serviceConnection,
                                Context.BIND_AUTO_CREATE
                            )
                        } else {
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
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "싱글 1km"
            }
            1 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "싱글 2km"
            }
            2 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "싱글 3km"
            }
            3 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                "싱글 5km"
            }
            4 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 50
                checkCount = 1
                "경쟁 1km"
            }
            5 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "경쟁 2km"
            }
            6 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "경쟁 3km"
            }
            7 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                "경쟁 5km"
            }
            8 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "협동 1km"
            }
            9 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "협동 2km"
            }
            10 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "협동 3km"
            }
            11 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
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
        binding.btnRunningPause.setOnClickListener {
            pauseRunning()
        }
        binding.btnRunningPlay.setOnClickListener {
            reStartRunning()
        }
        binding.btnRunningStop.setOnClickListener {
            stopRunning()
        }
        binding.sbMyProgress.max = maxDistance
        binding.sbMyProgress.setOnTouchListener { _, _ ->
            true
        }
        binding.sbPartnerProgress.max = maxDistance
        binding.sbPartnerProgress.setOnTouchListener { _, _ ->
            true
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter("custom-event"))
        sensorManager.registerListener(this, altitudeSensor, SensorManager.SENSOR_DELAY_NORMAL)
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
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent) {
        if (p0.sensor.type == Sensor.TYPE_PRESSURE && sensorTime == 120) {
            val altitude = SensorManager.getAltitude(
                SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                p0.values[0]
            )
            sensorTime = 0
            // 초기값 생성
            if (currentHeight == 0f) currentHeight = altitude
            // 100 -> 99면 1, 99 -> 100이면 -1
            val calcHeight = currentHeight - altitude
            printHeight = calcHeight - (calcHeight * 2f)
            binding.tvRunningHeight.text = "${printHeight.toInt()}"
        } else {
            sensorTime++
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun stopRunning() {
        val dialog = DefaultAlertDialog(
            AlertModel("알림 메시지", "러닝을 정지하시겠습니까?\n게임이 종료됩니다.", "확인")
        ) {
            // 타이머 중지
            handler.removeCallbacks(timer)
            // 거리, 페이스, 속력 중지 -> 서비스 종료
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            // 고도 중지
            sensorManager.unregisterListener(this)

            RunningAMQPManager.sendEndGame(getMessageForEndQueue())
            Log.d(TAG, "stopRunning: ${getMessageForEndQueue()}")

            navigate(
                RunningFragmentDirections.actionRunningFragmentToRunningFinishFragment(
                    locationList = locationList.toTypedArray(),
                    result = RunningFinishModel(
                        success = 0,
                        velocity = kmPerHour,
                        calorie = currentCalorie,
                        height = printHeight,
                        userId = args.myId,
                        raceId = args.roomId,
                        mode = args.gameType,
                        time = time
                    )
                )
            )
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun pauseRunning() {
        binding.btnRunningPlay.visibility = View.VISIBLE
        binding.btnRunningStop.visibility = View.VISIBLE
        binding.btnRunningPause.visibility = View.INVISIBLE
        // 타이머 일시중지
        handler.removeCallbacks(timer)
        // 거리, 페이스, 속력 일시중지 -> 브로드캐스트 리시버를 멈춤
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        // 고도 일시 중지
        sensorManager.unregisterListener(this)
    }

    private fun reStartRunning() {
        binding.btnRunningPlay.visibility = View.GONE
        binding.btnRunningStop.visibility = View.GONE
        binding.btnRunningPause.visibility = View.VISIBLE
        handler.postDelayed(timer, 1000)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter("custom-event"))
        sensorManager.registerListener(this, altitudeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}