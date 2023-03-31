package com.d204.rumeet.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class RunningService : Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var lastLocation: Location? = null
    private var totalDistance = 0f

    private val binder = RunningBinder()

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onLocationChanged(location: Location) {
        lastLocation?.let {
            val distance = it.distanceTo(location)
            totalDistance += distance

            // 현재 내 위도,경도와 총 뛴거리를 표시
            val intent = Intent("custom-event")
            intent.putExtra("distance", totalDistance)
            intent.putExtra("location", it)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
        lastLocation = location
    }

    fun getDistance(): Float {
        return totalDistance
    }
}