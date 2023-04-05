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
import android.os.Vibrator
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
import com.d204.rumeet.ui.running.option.model.RunningDifficulty
import com.d204.rumeet.util.*
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "러밋_RunningFragment"

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding, RunningViewModel>(),
    SensorEventListener {

    override val layoutResourceId: Int get() = R.layout.fragment_running
    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running) { defaultViewModelProviderFactory }

    private lateinit var sensorManager: SensorManager
    private lateinit var altitudeSensor: Sensor
    private lateinit var difficulty: RunningDifficulty
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

    private var distanceCheck = true

    private var gender = -1
    private var weight = 0f
    private var age = 0

    private var sensorTime = 0
    private var currentHeight = 0f

    private var kmPerHour: Float = 0f
    private var currentCalorie = 0f
    private var printHeight = 0f
    private var currentDistance = 0f
    private var collaborationDistance = 0
    private var testDistance = 300

    private lateinit var vibrator: Vibrator

    private var isMulti = false
    private var isGhost = false
    private var isShark = false

    // 서비스 연결여부 콜백함수
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

    /** BroadCastReceiver로 받은 데이터를 처리(속도) */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // 10m마다 값을 불러온다. 나오는 값은 10.123123123...
            val runningDistance = intent?.getFloatExtra("distance", 0f) ?: 0f
            val runningLocation = intent?.getParcelableExtra<Location>("location")

            // 거리를 TextView에 표시(소숫점 2자리아래 올림)
            binding.tvRunningDistance.text = floatTo2f(roundDigit(runningDistance.div(1000f).toDouble(),2).toFloat())
            // 좌표 기록
            locationList.add(runningLocation ?: throw IllegalAccessException("NO LOCATION"))

            currentDistance = runningDistance
            if(isShark) {
                collaborationDistance = ((runningDistance.toInt() + userDistance) / 2)
                binding.sbMyProgress.progress = collaborationDistance
            } else {
                binding.sbMyProgress.progress = runningDistance.toInt()
            }
            // 나의 seekbar 진행률을 올린다
            Log.d(TAG, "onReceive: my max progress : ${binding.sbMyProgress.max}")
            Log.d(TAG, "onReceive: currnet my distance : ${runningDistance.toInt()}")
            Log.d(TAG, "onReceive: max / current ${binding.sbMyProgress.max} / ${binding.sbMyProgress.progress}")

            // km를 시간으로 나눔 (3.6은 3600/1000)
            kmPerHour = runningLocation.speed * 3.6f
            // 칼로리 계산
            currentCalorie += getCalorie(gender, age, weight, time).toFloat()

            Log.d(TAG, "onReceive: kmPerHour $kmPerHour")
            Log.d(TAG, "onReceive: calorie $currentCalorie")

            // Textview에 뿌려줌
            binding.tvRunningPace.text = floatTo2f(kmPerHour)
            binding.tvRunningCalorie.text = floatTo2f(currentCalorie)

            // 페이스 기록(50은 테스트용)
            if (runningDistance >= testDistance.toFloat() && !pace1Flag) {
                pace1 = time.div(1000).toInt()
                pace1Flag = true
                Log.d(TAG, "onReceive: running 1000 finish")
                vibrator.vibrate(500)
            } else if (runningDistance >= 2000f && !pace2Flag) {
                pace2 = time.div(1000).toInt()
                pace2Flag = true
                Log.d(TAG, "onReceive: running 2000 finish")
                vibrator.vibrate(500)
            } else if (runningDistance >= 3000f && !pace3Flag) {
                pace2 = time.div(1000).toInt()
                pace2Flag = true
                Log.d(TAG, "onReceive: running 3000 finish")
                vibrator.vibrate(500)
            }

            // 싱글이면 메세지 보낼 필요 없음
            if(args.gameType >= 4){
                RunningAMQPManager.sendRunning(
                    args.partnerId,
                    args.roomId,
                    runningDistance.toInt().toString()
                )
            }

            //  러닝 종료
            if ((!isShark && maxDistance <= runningDistance) || (isShark && maxDistance <= collaborationDistance)) {
                Log.d(TAG, "onReceive: running 5000 end")
                // 5인경우 기록
                if (!pace5Flag) pace5 = time.div(1000).toInt()

                // 싱글이면 보낼 필요 없음
                if(args.gameType >= 4){
                    RunningAMQPManager.sendEndGame(getMessageForEndQueue())
                }

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
    private var sec = 0
    private lateinit var ghostPace : IntArray
    private var sequence = 0
    private var ghostDistance = 0
    private var sharkDistance = 0
    private var userDistance = 0
    private var sharkPace = 0
    /** 시간초 타이머 */
    private val handler = Handler(Looper.getMainLooper())
    private val timer = object : Runnable {
        override fun run() {
            time += 1000
            binding.tvRunningTime.text = time.toMinute()
            handler.postDelayed(this, 1000)
            // 고스트 모드 처리
            // 상어도 여기서 처리하면 될듯?
            if(isGhost) {
                sec++
                if(args.pace[sequence]==sec) {
                    sequence++
                }
                ghostDistance += ghostPace[sequence]
                Log.d(TAG, "run: ghostDistance = ${ghostDistance}")
                successRunningData(ghostDistance)
            }
            if(args.gameType >=8) {
                if(sec == 0) {
                    var shark = arrayOf(0,0,400,300,240)
                    sharkPace = 1000 / shark[args.gameType/4]
                    binding.sbSharkProgress.visibility = View.VISIBLE
                }
                sec++
                if(sec >= 30) {
                    if(sec == 30) {
                        Snackbar.make(binding.tvRunningMode, "상어가 출발합니다!!", Snackbar.LENGTH_SHORT).show()
                        vibrator.vibrate(1000)
                    }
                    Log.d(TAG, "run: sharkPace ${sharkPace}")
                    sharkDistance += sharkPace
                }
                successSharkData(sharkDistance)
            }
        }
    }

    /** end.queue에 보낼 메시지 생성 */
    private fun getMessageForEndQueue() : String{
        Log.d(TAG, "getMessageForEndQueue: maxDistance ${maxDistance}")
        val message = when (maxDistance) {
            1000 -> {
                Log.d(TAG, "onReceive: make 1000 response")
                val response = runningEndModel as RunningModel1pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if(pace1 != 0) response.pace1 = pace1
                Gson().toJson(response)
            }
            2000 -> {
                Log.d(TAG, "onReceive: make 2000 response")
                val response = runningEndModel as RunningModel2pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if(pace1 != 0)response.pace1 = pace1
                if(pace2 != 0)response.pace2 = pace2
                Gson().toJson(response)
            }
            3000 -> {
                Log.d(TAG, "onReceive: make 3000 response")
                val response = runningEndModel as RunningModel3pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if(pace1 != 0)response.pace1 = pace1
                if(pace2 != 0)response.pace2 = pace2
                if(pace3 != 0) response.pace3 = pace3
                Gson().toJson(response)
            }
            5000 -> {
                Log.d(TAG, "onReceive: make 5000 response")
                val response = runningEndModel as RunningModel5pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if(pace1 != 0)response.pace1 = pace1
                if(pace2 != 0)response.pace2 = pace2
                if(pace3 != 0)response.pace3 = pace3
                if(pace5 != 0) response.pace5 = pace5
                Gson().toJson(response)
            }
            else -> throw java.lang.IllegalArgumentException("NO MAX DISTANCE")
        }
        Log.d(TAG, "getMessageForEndQueue: $message")
        return message
    }

    // 상대방과 나의 profile 이미지로 seekbar의 thumb 이미지 변경
    override fun initStartView() {
        // Todo 싱글, 고스트 설정을 해줘야함
        viewModel.getUserInfo(args.myId)

        if(args.gameType >= 4){ // multi
            Log.d(TAG, "initStartView: @@@@@@@멀티모드 경기 시작")
            isMulti = true

            with(binding){
                sbMyProgress.visibility = View.VISIBLE
                if(args.gameType<8) {
                    sbPartnerProgress.visibility = View.VISIBLE
                    sbSharkProgress.visibility = View.GONE
                } else {
                    sbPartnerProgress.visibility = View.GONE
                    sbSharkProgress.visibility = View.VISIBLE
                }
                binding.btnRunningStop.visibility = View.VISIBLE
            }
        }  else {
            isGhost = true
            binding.btnRunningStop.visibility = View.VISIBLE
            if(args.partnerId != -1){ // 고스트 모드
                viewModel.getPartnerInfo(args.partnerId)
                ghostPace = IntArray(args.pace.size)
                binding.sbPartnerProgress.visibility = View.VISIBLE
                for(i in ghostPace.indices) {
                    Log.d(TAG, "initStartView: (1000/args.pace[0]).toDouble() = ${(1000.0/args.pace[i])}")
                    ghostPace[i] = Math.round((1000.0/args.pace[i])).toInt()// 1 더주는 이유는 올림 처리
                }
                Log.d(TAG, "initStartView: ghostPace= ${ghostPace.contentToString()}")
            }
        }



        // 고도 센서 설정
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        altitudeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        initRunningMode()

        Log.d(TAG, "initStartView: ##########maxDistance $maxDistance")

        // 고스트 모드일 경우 seekbar 자동으로 움직이게 하기

    }
    private fun successSharkData(distance : Int) {
        binding.sbSharkProgress.progress = distance
        // 상어한테 먹힘
        if(collaborationDistance < distance){
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

    private fun successRunningData(distance : Int){
        Log.d(TAG, "initDataBinding: partner running : ${distance}")
        userDistance = distance
        if(args.gameType>=8)  {
            collaborationDistance = (currentDistance.toInt() + distance) / 2
            binding.sbMyProgress.progress = collaborationDistance;
        } else {
            binding.sbMyProgress
            binding.sbPartnerProgress.progress = distance
            // 거리를 따라 잡혔으면 알람
            if(currentDistance < distance && distanceCheck){
                Snackbar.make(binding.tvRunningMode, "따라잡혔습니다!!", Snackbar.LENGTH_SHORT).show()
                vibrator.vibrate(500)
                distanceCheck = false
            } else if(currentDistance > distance){
                distanceCheck = true
            }

            // 상대방의 거리를 받아 더 커진다면?
            if (distance >= maxDistance) {
                // end queue에 메시지
                Log.d(TAG, "initDataBinding: end game you lose ${getMessageForEndQueue()}")

                if(isMulti) { // 게임 종료 보내기
                    RunningAMQPManager.sendEndGame(getMessageForEndQueue())
                }


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

    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.runningSideEffect.collectLatest {
                    when (it) {
                        is RunningSideEffect.SuccessSoloData -> {
                            // 필요없는거임
                        }

                        is RunningSideEffect.SuccessRunning -> {
                            // 경쟁할 때 rabbitMQ의 콜백으로 온 데이터 받음
                            successRunningData(it.distance)
                        }

                        is RunningSideEffect.EndRunning -> {

                        }

                        is RunningSideEffect.SuccessPartnerInfo -> {
                            // 파트너의 프로필을 seekbar의 thumb로 변경
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
                            // 나의 프로필 이미지를 seekbar의 thumb로 변경
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

                            // Todo 경쟁이면 해당 코드 실행
                            if(args.gameType >= 4){
                                viewModel.startRun(args.myId, args.roomId)
                                Log.d("TAG", "SuccessUserInfo: start")
                            }

                            // 사용자의 정보 생성(칼로리 받기 위함)
                            gender = it.userInfo.gender
                            age = it.userInfo.age
                            weight = it.userInfo.weight.toFloat()

                            // 서비스 실행
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
            launch {
                viewModel
            }
        }
    }

    /** 러닝 기본 데이터 생성 */
    private fun initRunningMode() {
        binding.tvRunningMode.text = when (args.gameType) {
            0 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                if(isGhost) "고스트 1km"
                else "싱글 1km"
            }
            1 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                if(isGhost) "고스트 2km"
                else "싱글 2km"
            }
            2 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                if(isGhost) "고스트 3km"
                else "싱글 3km"
            }
            3 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                if(isGhost) "고스트 5km"
                else "싱글 5km"
            }
            4 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 1000
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
            12 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "협동 1km"
            }
            13 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "협동 2km"
            }
            14 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "협동 3km"
            }
            15 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                "협동 5km"
            }
            16 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 1000
                checkCount = 1
                "협동 1km"
            }
            17 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                "협동 2km"
            }
            18 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                "협동 3km"
            }
            19 -> {
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

    /** 타이머 실행 및 버튼 이벤트, SeekBar의 이벤트 막기 */
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
        sensorManager.unregisterListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(timer)
        activity?.unbindService(serviceConnection)
        sensorManager.unregisterListener(this)
    }

    /** 고도센서 변경 */
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

            Log.d(TAG, "stopRunning: 게임 종료하고 이제 화면 넘어갑니다.")
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