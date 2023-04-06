package com.d204.rumeet.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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

class RunningService : Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var lastLocation: Location? = null
    private var totalDistance = 0f
    private val binder = RunningBinder()

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
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L, 0f, this)
        initKalmanFilter()

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

    var times = -1
    override fun onLocationChanged(location: Location) {
        times++
        if(times < 2) {
            latitudeFilter.filter(location.latitude.toFloat())
            latitudeFilter.filter(location.latitude.toFloat())
            latitudeFilter.filter(location.latitude.toFloat())
            longitudeFilter.filter(location.longitude.toFloat())
            longitudeFilter.filter(location.longitude.toFloat())
            longitudeFilter.filter(location.longitude.toFloat())
            latitudeFilter.filter(location.latitude.toFloat())
            latitudeFilter.filter(location.latitude.toFloat())
            latitudeFilter.filter(location.latitude.toFloat())
            longitudeFilter.filter(location.longitude.toFloat())
            longitudeFilter.filter(location.longitude.toFloat())
            longitudeFilter.filter(location.longitude.toFloat())
            location.latitude = latitudeFilter.filter(location.latitude.toFloat()).toDouble()
            location.longitude =longitudeFilter.filter(location.longitude.toFloat()).toDouble()
            return
        }
        location.latitude = latitudeFilter.filter(location.latitude.toFloat()).toDouble()
        location.longitude = longitudeFilter.filter(location.longitude.toFloat()).toDouble()
        Log.d("TAG", "Change!!!!!!!!: ${times}")
//        if(times == 0) {
//            times = -1
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
        if (provider?.equals(LocationManager.GPS_PROVIDER) == true) {
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
                        1000L,
                        10.0f,
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
        }
    }
    private lateinit var latitudeFilter: KalmanFilter
    private lateinit var longitudeFilter: KalmanFilter

    private fun initKalmanFilter() {
        // 칼만 필터를 초기화하고 적절한 노이즈 값을 설정합니다.
        val measurementNoise = 1f
        val processNoise = 1f
        latitudeFilter = KalmanFilter(measurementNoise, processNoise)
        longitudeFilter = KalmanFilter(measurementNoise, processNoise)
    }

    class KalmanFilter(private val r: Float, private val q: Float) {
        private var x = 0f
        private var p = 1f

        fun filter(z: Float): Float {
            p += q
            val k = p / (p + r)
            x += k * (z - x)
            p *= (1 - k)
            return x
        }
    }
}