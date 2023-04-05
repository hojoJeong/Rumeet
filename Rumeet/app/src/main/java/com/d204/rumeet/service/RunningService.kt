package com.d204.rumeet.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
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
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 10.0f, this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_toolbar_logo)
            .setContentTitle("제목1")
            .setContentText("내용1")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


    override fun onLocationChanged(location: Location) {
        lastLocation?.let {
            val distance = it.distanceTo(location)
            totalDistance += distance

            // 현재 내 위도,경도와 총 뛴거리를 표시
            val intent = Intent("custom-event")
            intent.putExtra("distance", totalDistance)
            intent.putExtra("location", it)
            Log.d("TAG", "onLocationChanged: broadcast")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
        lastLocation = location
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
}