package com.d204.rumeet.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.*
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.d204.rumeet.R
import com.d204.rumeet.util.floatTo2f

class RunningService : Service(), LocationListener, SensorEventListener {

    private lateinit var locationManager: LocationManager
    private var lastLocation: Location? = null
    private var totalDistance = 0f
    private val binder = RunningBinder()
    private var currentStepCount = 0;
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounter: Sensor

    companion object {
        const val NOTIFICATION_ID = 10
        const val CHANNEL_ID = "RumeetServiceChannel"
    }
    private fun createCriteria() = Criteria().apply {
        accuracy = Criteria.ACCURACY_FINE
        powerRequirement = Criteria.POWER_HIGH
        isAltitudeRequired = true
        isSpeedRequired = true
        isCostAllowed = false
        isBearingRequired = false
        horizontalAccuracy = Criteria.ACCURACY_HIGH
        verticalAccuracy = Criteria.ACCURACY_HIGH
    }
    inner class RunningBinder : Binder() {
        fun getService(): RunningService {
            return this@RunningService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "권환 필요", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("TAG", "실행은됨 @@@@@@@@@ onCreate: ")
        createCriteria()
//        initKalmanFilter()

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L, 0f, this)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100L, 0f, this)
        try {
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL)
        } catch (e : java.lang.Exception) {
            Log.d("s", "onCreate: ")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "test", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            startForeground(2, createNotification())
        } else {
            // API 레벨이 26 미만인 경우, startForeground() 메서드를 호출할 필요 없음
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
        lastLocation = null
        totalDistance = 0f
        stopForeground(true)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        locationManager.removeUpdates(this)
        lastLocation = null
        totalDistance = 0f
        return super.onUnbind(intent)
    }


    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_toolbar_logo)
        .setContentTitle("Rumeet")
        .setChannelId(CHANNEL_ID)
        .setContentText("러밋과 함께 달리는 중이에요!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    var times = 0
    var mvLat = 0f
    var mvLng = 0f
    var mvSpeed = 0f
    var init = true
    var prevStep = 0


    override fun onLocationChanged(location: Location) {
        var mvLocation = location
        Log.d("TAG", "onLocationChanged: @@@@@@@@@@@@@")
        if(times < 2) {
            mvLat += location.latitude.toFloat()
            mvLng += location.longitude.toFloat()
            mvSpeed += location.speed
            times++
            return
        }
        if(init) {
            latitudeFilter = KalmanFilter(mvLat/2,1f, 1f)
            longitudeFilter = KalmanFilter(mvLng/2,1f, 1f)
            speedFilter = KalmanFilter(4f,1f, 1f)
            init = false
        }
        times = 0
        location.latitude = latitudeFilter.filter(mvLat/2).toDouble()
        location.longitude = longitudeFilter.filter(mvLng/2).toDouble()
        location.speed = speedFilter.filter(mvSpeed/2)
        mvLat = 0f
        mvLng = 0f
        mvSpeed = 0f
        Log.d("TAG", "Change!!!!!!!!: ${times}")
        Log.d("TAG", "Change!!!!!!!!: ${location.latitude} , ${location.longitude} , Speed : ${location.speed}")

//        if(times == 0) {
//            times = -1
//        if(prevStep == currentStepCount) {
//            Toast.makeText(this, "걸음센서 없음", Toast.LENGTH_SHORT).show()
//            Log.d("TAG", "onLocationChanged: 걸음센서 없음!")
//            return
//        }
        prevStep = currentStepCount
        lastLocation?.let {
            val distance = it.distanceTo(location)
            Log.d("TAG", "distance: ${distance}")
            if(distance >= 10) {
                return@let
            }
            totalDistance += distance
            Log.d("TAG", "total distance: ${totalDistance}")

            // 현재 내 위도,경도와 총 뛴거리를 표시
            val intent = Intent("custom-event")
            intent.putExtra("distance", totalDistance)
            intent.putExtra("location", it)
            Log.d("TAG", "onLocationChanged: broadcast")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
        lastLocation = location
//        }
    }


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("TAG", "onStatusChanged: @@@@@@@@@@@@@@@@@@@")
        /*if (provider?.equals(LocationManager.GPS_PROVIDER) == true) {
            when (status) {
                LocationProvider.AVAILABLE -> {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        100L,
                        0f,
                        this
                    )
                }
                LocationProvider.OUT_OF_SERVICE -> {
                    locationManager.removeUpdates(this)
                }
                LocationProvider.TEMPORARILY_UNAVAILABLE -> {
                    locationManager.removeUpdates(this)
                }
            }
        }*/
    }
    private lateinit var latitudeFilter: KalmanFilter
    private lateinit var longitudeFilter: KalmanFilter
    private lateinit var speedFilter: KalmanFilter

//    private fun initKalmanFilter(v) {
//        // 칼만 필터를 초기화하고 적절한 노이즈 값을 설정합니다.
//        val measurementNoise = 1f
//        val processNoise = 1f
//        latitudeFilter = KalmanFilter(measurementNoise, processNoise)
//        longitudeFilter = KalmanFilter(measurementNoise, processNoise)
//        speedFilter = KalmanFilter(measurementNoise, processNoise)
//    }

    class KalmanFilter(private val xx: Float,private val r: Float, private val q: Float) {
        private var x = xx
        private var p = 1f

        fun filter(z: Float): Float {
            p += q
            val k = p / (p + r)
            x += k * (z - x)
            p *= (1 - k)
            return x
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        currentStepCount = p0!!.values[0].toInt()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}