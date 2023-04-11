package com.d204.rumeet.ui.running

import android.content.*
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.media.MediaPlayer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import android.media.MediaRecorder
import android.os.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.d204.rumeet.util.extension.repeatOnStarted
import java.io.File


private const val TAG = "러밋_RunningFragment"

@AndroidEntryPoint
class RunningFragment : BaseFragment<FragmentRunningBinding, RunningViewModel>(),
    SensorEventListener {

    override val layoutResourceId: Int get() = R.layout.fragment_running
    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running) { defaultViewModelProviderFactory }
    private var mediaRecorder: MediaRecorder? = null
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
    private var collaborationDistance = 0f
    private var testDistance = 200

    private lateinit var vibrator: Vibrator

    private var isMulti = false
    private var isGhost = false
    private var isShark = false
    private var isGameSet = false
    private var isLose = false

    private val runningIntent by lazy { Intent(activity, RunningService::class.java) }

    // 서비스 연결여부 콜백함수
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RunningService.RunningBinder
            runningService = binder.getService()
            bindState = true
            Log.d(TAG, "onServiceConnected: bindService")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bindState = false
            Log.d(TAG, "onServiceDisconnected: disconnect")
        }
    }
    var xy = BooleanArray(600)
    /** BroadCastReceiver로 받은 데이터를 처리(속도) */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            // 10m마다 값을 불러온다. 나오는 값은 10.123123123...
            val runningDistance = intent?.getFloatExtra("distance", 0f) ?: 0f
            val runningLocation = intent?.getParcelableExtra<Location>("location")

            // 거리를 TextView에 표시(소숫점 2자리아래 올림)
            binding.tvRunningDistance.text =
                floatTo2f(roundDigit(runningDistance.div(1000f).toDouble(), 2).toFloat())
            // 좌표 기록
            Log.d(TAG, "onReceive: service receive!! ${runningDistance}")

            currentDistance = runningDistance
            if (isShark) {
                collaborationDistance = ((runningDistance + userDistance) / 2)
                binding.sbMyProgress.progress = collaborationDistance.toInt()
            } else {
                binding.sbMyProgress.progress = runningDistance.toInt()
            }
            if(!xy[currentDistance.toInt()/10]){
                xy[currentDistance.toInt()/10] = true
                locationList.add(runningLocation ?: throw IllegalAccessException("NO LOCATION"))
            }
            // 나의 seekbar 진행률을 올린다
            Log.d(TAG, "onReceive: my max progress : ${binding.sbMyProgress.max}")
            Log.d(TAG, "onReceive: currnet my distance : ${runningDistance.toInt()}")
            Log.d(
                TAG,
                "onReceive: max / current ${binding.sbMyProgress.max} / ${binding.sbMyProgress.progress}"
            )

            // km를 시간으로 나눔 (3.6은 3600/1000)
            kmPerHour = runningLocation!!.speed * 3.6f
            // 칼로리 계산
            currentCalorie = getCalorie(gender, age, weight, time).toFloat()

            if (kmPerHour > 10) {

            } else {

            }

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
            Log.d(TAG, "onReceive: args.gameType ${args.gameType }")
            // 싱글이면 메세지 보낼 필요 없음
            if (args.gameType >= 4) {
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
                if (args.gameType >= 4) {
                    RunningAMQPManager.sendEndGame(getMessageForEndQueue())
                }

                isGameSet = true

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
    private lateinit var ghostPace: IntArray
    private var sequence = 0
    private var ghostDistance = 0
    private var sharkDistance = 0
    private var userDistance = 0
    private var sharkPace = 0
    private var mictime = -5000L
    /** 시간초 타이머 */
    private val handler = Handler(Looper.getMainLooper())
    private val timer = object : Runnable {
        override fun run() {
            time += 1000
            Log.d(TAG, "run: timer : ${time}")
            binding.tvRunningTime.text = time.toMinute()
            handler.postDelayed(this, 1000)
            // 고스트 모드 처리
            // 상어도 여기서 처리하면 될듯?
            if (isGhost) {
                sec++
                if (args.pace[sequence] == sec) {
                    sequence++
                }
                if (!::ghostPace.isInitialized) {
                    ghostPace = IntArray(args.pace.size)
                    binding.sbPartnerProgress.visibility = View.VISIBLE
                    for (i in ghostPace.indices) {
                        ghostPace[i] = Math.round((1000.0 / args.pace[i])).toInt()// 1 더주는 이유는 올림 처리
                    }
                }
                ghostDistance += ghostPace[sequence]
                Log.d(TAG, "run: ghostDistance = ${ghostDistance}")
                successRunningData(ghostDistance)
            }
            if (args.gameType >= 8) {
                if (sec == 0) {
                    isShark = true
                    var shark = arrayOf(0, 0, 400, 300, 240)
                    sharkPace = 1000 / shark[args.gameType / 4]
                    Log.d(TAG, "run: shark visibility visible")
                }
                sec++
                if (sec >= 30) {
                    if (sec == 30) {
                        binding.sbSharkProgress.visibility = View.VISIBLE
                        Snackbar.make(binding.tvRunningMode, "상어가 출발합니다!!", Snackbar.LENGTH_SHORT)
                            .show()
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
    private fun getMessageForEndQueue(): String {
        Log.d(TAG, "getMessageForEndQueue: maxDistance ${maxDistance}")
        val message = when (maxDistance) {
            testDistance -> {
                Log.d(TAG, "onReceive: make 1000 response")
                val response = runningEndModel as RunningModel1pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if (pace1 != 0) response.pace1 = pace1
                Gson().toJson(response)
            }
            2000 -> {
                Log.d(TAG, "onReceive: make 2000 response")
                val response = runningEndModel as RunningModel2pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if (pace1 != 0) response.pace1 = pace1
                if (pace2 != 0) response.pace2 = pace2
                Gson().toJson(response)
            }
            3000 -> {
                Log.d(TAG, "onReceive: make 3000 response")
                val response = runningEndModel as RunningModel3pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if (pace1 != 0) response.pace1 = pace1
                if (pace2 != 0) response.pace2 = pace2
                if (pace3 != 0) response.pace3 = pace3
                Gson().toJson(response)
            }
            5000 -> {
                Log.d(TAG, "onReceive: make 5000 response")
                val response = runningEndModel as RunningModel5pace
                response.user_id = args.myId
                response.race_id = args.roomId
                if (pace1 != 0) response.pace1 = pace1
                if (pace2 != 0) response.pace2 = pace2
                if (pace3 != 0) response.pace3 = pace3
                if (pace5 != 0) response.pace5 = pace5
                Gson().toJson(response)
            }
            else -> throw java.lang.IllegalArgumentException("NO MAX DISTANCE")
        }
        Log.d(TAG, "getMessageForEndQueue: $message")
        return message
    }

    private var isMute = false

    // 상대방과 나의 profile 이미지로 seekbar의 thumb 이미지 변경
    override fun initStartView() {

        binding.btnRunningSound.setOnCheckedChangeListener { _, isChecked ->
            isMute = !isChecked
        }

        // Todo 싱글, 고스트 설정을 해줘야함
        Log.d(TAG, "initStartView: ${args.partnerId}")
        viewModel.getUserInfo(args.myId)

        if (args.gameType >= 4) { // multi
            Log.d(TAG, "initStartView: @@@@@@@멀티모드 경기 시작")
            isMulti = true

            viewModel.getPartnerInfo(args.partnerId)

            with(binding) {
                sbMyProgress.visibility = View.VISIBLE
                btnMic.visibility = View.VISIBLE
                if (args.gameType < 8) {
                    sbPartnerProgress.visibility = View.VISIBLE
                    sbSharkProgress.visibility = View.GONE
                } else {
                    // 협동
                    sbPartnerProgress.visibility = View.GONE
                    Glide.with(requireContext())
                        .asGif()
                        .override(85, 85)
                        .load(R.drawable.ic_together_running_animation)
                        .into(object : CustomTarget<GifDrawable>() {
                            override fun onResourceReady(
                                resource: GifDrawable,
                                transition: Transition<in GifDrawable>?
                            ) {
                                resource.setBounds(0, -500, 0, 0)
                                binding.sbMyProgress.thumb = resource
                                resource.start()
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                }
                binding.btnRunningStop.visibility = View.VISIBLE
            }
        } else { // single
            if (args.partnerId != -1) { // ghost
                isGhost = true

                Glide.with(requireContext())
                    .asGif()
                    .override(120, 120)
                    .load(R.drawable.ic_ghost_animation)
                    .into(object : CustomTarget<GifDrawable>() {
                        override fun onResourceReady(
                            resource: GifDrawable,
                            transition: Transition<in GifDrawable>?
                        ) {
                            Log.d(TAG, "onResourceReady: shark")
                            binding.sbPartnerProgress.thumb = resource
                            resource.start()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })

                binding.btnRunningStop.visibility = View.VISIBLE
                binding.btnRunningPause.visibility = View.GONE
                ghostPace = IntArray(args.pace.size)
                binding.sbPartnerProgress.visibility = View.VISIBLE
                for (i in ghostPace.indices) {
                    Log.d(
                        TAG,
                        "initStartView: (1000/args.pace[0]).toDouble() = ${(1000.0 / args.pace[i])}"
                    )
                    ghostPace[i] = Math.round((1000.0 / args.pace[i])).toInt()// 1 더주는 이유는 올림 처리
                }
                Log.d(TAG, "initStartView: ghostPace= ${ghostPace.contentToString()}")
            } else { // solo
                binding.btnRunningPause.visibility = View.VISIBLE
                binding.sbPartnerProgress.visibility = View.GONE
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

    private fun successSharkData(distance: Int) {
        binding.sbSharkProgress.progress = distance
        // 상어한테 먹힘
        if (collaborationDistance < distance) {
            RunningAMQPManager.sendEndGame(getMessageForEndQueue())
            navigate(
                RunningFragmentDirections.actionRunningFragmentToRunningFinishFragment(
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
                )
            )
        }
    }

    private fun successRunningData(distance: Int) {
        Log.d(TAG, "initDataBinding: partner running : ${distance}")
        userDistance = distance
        if (args.gameType >= 8) {
            collaborationDistance = (currentDistance + distance) / 2
            binding.sbMyProgress.progress = collaborationDistance.toInt()
        } else {
            binding.sbMyProgress
            binding.sbPartnerProgress.progress = distance
            // 거리를 따라 잡혔으면 알람
            if (currentDistance < distance && distanceCheck) {
                Snackbar.make(binding.tvRunningMode, "따라잡혔습니다!!", Snackbar.LENGTH_SHORT).show()
                vibrator.vibrate(500)
                distanceCheck = false
            } else if (currentDistance > distance) {
                distanceCheck = true
            }

            // 상대방의 거리를 받아 더 커진다면?
            if (distance >= maxDistance) {
                // end queue에 메시지
                Log.d(TAG, "initDataBinding: end game you lose ${getMessageForEndQueue()}")

                if (isMulti) { // 게임 종료 보내기
                    RunningAMQPManager.sendEndGame(getMessageForEndQueue())
                }


                navigate(
                    RunningFragmentDirections.actionRunningFragmentToRunningFinishFragment(
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
                    )
                )
            }
        }

    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.runningSideEffect.collectLatest {
                    when (it) {
                        is RunningSideEffect.SuccessSoloData -> {
                            // 필요없는거임
                        }

                        is RunningSideEffect.SuccessRunning -> {
                            // 경쟁할 때 rabbitMQ의 콜백으로 온 데이터 받음
                            Log.d(TAG, "initDataBinding: ")
                            successRunningData(it.distance)
                            // 백그라운드 상태에서 상대방이 먼저 옴을 확인
                            if (it.distance >= maxDistance) {
                                isLose = true
                            }
                        }

                        is RunningSideEffect.EndRunning -> {

                        }
                        is RunningSideEffect.SuccessAudio -> {
                            val tempFile = File.createTempFile("received_audio", ".mp3")
                            tempFile.writeBytes(it.file)

                            // MediaPlayer를 사용하여 임시 파일을 재생
                            val mediaPlayer = MediaPlayer().apply {
                                setDataSource(tempFile.absolutePath)
                                prepare()
                                if (!isMute) {
                                    start()
                                }

                                setOnCompletionListener {
                                    // 재생 완료 시 리소스 해제 및 임시 파일 삭제
                                    release()
                                    tempFile.delete()
                                }
                            }
                        }

                        is RunningSideEffect.SuccessPartnerInfo -> {

                            Glide.with(requireContext())
                                .asGif()
                                .override(100, 100)
                                .load(R.drawable.ic_partner_running_animation)
                                .into(object : CustomTarget<GifDrawable>() {
                                    override fun onResourceReady(
                                        resource: GifDrawable,
                                        transition: Transition<in GifDrawable>?
                                    ) {
                                        Log.d(TAG, "onResourceReady: shark")
                                        binding.sbPartnerProgress.thumb = resource
                                        resource.start()
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                })
                        }

                        is RunningSideEffect.SuccessUserInfo -> {
                            Log.d(TAG, "SuccessUserInfo: SuccessUserInfo")
                            if (args.gameType < 8) {
                                Glide.with(requireContext())
                                    .asGif()
                                    .override(100, 100)
                                    .load(R.drawable.ic_my_running_animation)
                                    .into(object : CustomTarget<GifDrawable>() {
                                        override fun onResourceReady(
                                            resource: GifDrawable,
                                            transition: Transition<in GifDrawable>?
                                        ) {
                                            Log.d(TAG, "onResourceReady: shark")
                                            binding.sbMyProgress.thumb = resource
                                            resource.start()
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {

                                        }
                                    })
                            }

                            Glide.with(requireContext())
                                .asGif()
                                .override(100, 100)
                                .load(R.drawable.ic_shark_animation)
                                .into(object : CustomTarget<GifDrawable>() {
                                    override fun onResourceReady(
                                        resource: GifDrawable,
                                        transition: Transition<in GifDrawable>?
                                    ) {
                                        Log.d(TAG, "onResourceReady: shark")
                                        binding.sbSharkProgress.thumb = resource
                                        resource.start()
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                })

                            // Todo 경쟁이면 해당 코드 실행
                            if (args.gameType >= 4) {
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
                                requireActivity().bindService(
                                    runningIntent,
                                    serviceConnection,
                                    Context.BIND_AUTO_CREATE
                                )
                                ContextCompat.startForegroundService(
                                    requireContext(),
                                    runningIntent
                                )
                            } else {
                                Log.d("bindState", "initDataBinding: already start")
                            }
                        }
                    }
                }
            }
        }
    }

    /** 러닝 기본 데이터 생성 */
    private fun initRunningMode() {
        when (args.gameType) {
            0 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = testDistance
                checkCount = 1
                if (isGhost) {
                    binding.tvRunningMode.text = "고스트 모드"
                } else {
                    binding.tvRunningMode.text = "싱글 모드"
                }
                binding.tvRunningTotalDistance.text = "목표거리 : 1km"
            }
            1 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                if (isGhost) {
                    binding.tvRunningMode.text = "고스트 모드"
                } else {
                    binding.tvRunningMode.text = "싱글 모드"
                }
                binding.tvRunningTotalDistance.text = "목표거리 : 2km"
            }
            2 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                if (isGhost) {
                    binding.tvRunningMode.text = "고스트 모드"
                } else {
                    binding.tvRunningMode.text = "싱글 모드"
                }
                binding.tvRunningTotalDistance.text = "목표거리 : 3km"
            }
            3 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                if (isGhost) {
                    binding.tvRunningMode.text = "고스트 모드"
                } else {
                    binding.tvRunningMode.text = "싱글 모드"
                }
                binding.tvRunningTotalDistance.text = "목표거리 : 5km"
            }
            4 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = testDistance
                checkCount = 1
                binding.tvRunningMode.text = "경쟁 모드"
                binding.tvRunningTotalDistance.text = "목표거리 : 1km"
            }
            5 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                binding.tvRunningMode.text = "경쟁 모드"
                binding.tvRunningTotalDistance.text = "목표거리 : 2km"
            }
            6 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                binding.tvRunningMode.text = "경쟁 모드"
                binding.tvRunningTotalDistance.text = "목표거리 : 3km"
            }
            7 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                binding.tvRunningMode.text = "경쟁 모드"
                binding.tvRunningTotalDistance.text = "목표거리 : 5km"
            }
            8 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = testDistance
                checkCount = 1
                binding.tvRunningMode.text = "협동 모드 - Easy"
                binding.tvRunningTotalDistance.text = "목표거리 : 1km"
            }
            9 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                binding.tvRunningMode.text = "협동 모드 - Easy"
                binding.tvRunningTotalDistance.text = "목표거리 : 2km"
            }
            10 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                binding.tvRunningMode.text = "협동 모드 - Easy"
                binding.tvRunningTotalDistance.text = "목표거리 : 3km"
            }
            11 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                binding.tvRunningMode.text = "협동 모드 - Easy"
                binding.tvRunningTotalDistance.text = "목표거리 : 5km"
            }
            12 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = testDistance
                checkCount = 1
                binding.tvRunningMode.text = "협동 모드 - Normal"
                binding.tvRunningTotalDistance.text = "목표거리 : 1km"
            }
            13 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                binding.tvRunningMode.text = "협동 모드 - Normal"
                binding.tvRunningTotalDistance.text = "목표거리 : 2km"
            }
            14 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                binding.tvRunningMode.text = "협동 모드 - Normal"
                binding.tvRunningTotalDistance.text = "목표거리 : 3km"
            }
            15 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                binding.tvRunningMode.text = "협동 모드 - Normal"
                binding.tvRunningTotalDistance.text = "목표거리 : 5km"
            }
            16 -> {
                runningEndModel = RunningModel1pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = testDistance
                checkCount = 1
                binding.tvRunningMode.text = "협동 모드 - Hard"
                binding.tvRunningTotalDistance.text = "목표거리 : 1km"
            }
            17 -> {
                runningEndModel = RunningModel2pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 2000
                checkCount = 2
                binding.tvRunningMode.text = "협동 모드 - Hard"
                binding.tvRunningTotalDistance.text = "목표거리 : 2km"
            }
            18 -> {
                runningEndModel = RunningModel3pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 3000
                checkCount = 3
                binding.tvRunningMode.text = "협동 모드 - Hard"
                binding.tvRunningTotalDistance.text = "목표거리 : 3km"
            }
            19 -> {
                runningEndModel = RunningModel5pace(
                    user_id = args.myId,
                    race_id = args.roomId
                )
                maxDistance = 5000
                checkCount = 4
                binding.tvRunningMode.text = "협동 모드 - Hard"
                binding.tvRunningTotalDistance.text = "목표거리 : 3km"
            }
            else -> toastMessage("러닝 종류가 없음")
        }
    }
    var prevToast: Toast? = null
    var recTime = 0L
    /** 타이머 실행 및 버튼 이벤트, SeekBar의 이벤트 막기 */
    @RequiresApi(Build.VERSION_CODES.O)
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
        binding.sbSharkProgress.max = maxDistance
        binding.sbSharkProgress.setOnTouchListener { _, _ ->
            true
        }
        binding.sbTotalProgress.setOnTouchListener { _, _ ->
            true
        }
        binding.btnMic.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    var tmp = System.currentTimeMillis()
                    if(mictime + 3000 > tmp) {
                        prevToast?.cancel()
                        val newToast = Toast.makeText(context,"${(3000+mictime-tmp)/1000}초 뒤 사용가능",Toast.LENGTH_SHORT)
                        newToast.show()
                        prevToast = newToast
                    }else {
                        // 버튼이 눌렸을 때 녹음 시작
                        vibrator.vibrate(VibrationEffect.createOneShot(300, 60))
                        startRecording()
                    }
                    recTime = tmp

                    true
                }
                MotionEvent.ACTION_UP -> {
                    var tmp = System.currentTimeMillis()
                    if(recTime + 1000 <= tmp ) {
                        stopRecording()
                        RunningAMQPManager.sendAudioFile(
                            args.partnerId,
                            args.roomId,
                            audioFile.readBytes()
                        )
                        if (audioFile.exists()) {
                            audioFile.delete()
                        }
                    } else {
                        prevToast?.cancel()
                        val newToast = Toast.makeText(context,"꾹 눌러서 사용하세요",Toast.LENGTH_SHORT)
                        newToast.show()
                        prevToast = newToast
                    }
                    mictime = tmp
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
    var recCheck = false

    lateinit var audioFile: File
    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            val audioDir = context?.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            audioFile = File(audioDir, "rumeet.mp3")
            setOutputFile(audioFile.absolutePath)
            prepare()
            start()
            recCheck = true
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    override fun onResume() {
        super.onResume()

        // 백그라운드에서 처리 -> 게임이 끝났으면?
        if (isGameSet) {
            // 끝났는데 상대가 먼저 끝냈으면?
            if (isLose) {
                // 졌음
                navigate(
                    RunningFragmentDirections.actionRunningFragmentToRunningFinishFragment(
                        locationList = locationList.toTypedArray(),
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
                    )
                )
            } else {
                // 이겼음
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

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter("custom-event"))
        sensorManager.registerListener(this, altitudeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().stopService(runningIntent)
        requireActivity().unbindService(serviceConnection)
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        handler.removeCallbacks(timer)
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