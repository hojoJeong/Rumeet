package com.d204.rumeet.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.apache.commons.math3.filter.DefaultMeasurementModel
import org.apache.commons.math3.filter.DefaultProcessModel
import org.apache.commons.math3.filter.KalmanFilter
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealVector

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

class KalmanFilter2 {
    // 칼만 필터 파라미터 설정
    private val initialProcessNoise = 0.01
    private val initialMeasurementNoise = 3.0
    private val initialEstimationError = 10.0

    // 칼만 필터 적용을 위한 초기화
    private var processNoise = initialProcessNoise
    private var measurementNoise = initialMeasurementNoise
    private var estimationError = initialEstimationError
    private var lastLatitude = 0.0
    private var lastLongitude = 0.0

    fun filter(latitude: Double, longitude: Double, altitude: Double, accuracy: Float): Location {
        // 칼만 필터 적용을 위한 파라미터 계산
        val K = estimationError / (estimationError + measurementNoise)
        val lat = lastLatitude + K * (latitude - lastLatitude)
        val lon = lastLongitude + K * (longitude - lastLongitude)
        val estErr = (1 - K) * estimationError + processNoise
        lastLatitude = lat
        lastLongitude = lon
        estimationError = estErr
        val filteredLocation = Location("")
        filteredLocation.latitude = lat
        filteredLocation.longitude = lon
        filteredLocation.altitude = altitude
        return filteredLocation
    }
}

class GpsKalmanFilter {
    var kalmanFilter: KalmanFilter? = null

    fun initialize(initialState: RealVector) {
        val stateTransition = Array2DRowRealMatrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0, 1.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 1.0),
                doubleArrayOf(0.0, 0.0, 1.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 1.0)
            )
        )

        val control = null // No control input in this example

        val processNoise = Array2DRowRealMatrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 1.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 1.0)
            )
        )

        val initialStateCovariance = Array2DRowRealMatrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 1.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 1.0)
            )
        )

        val processModel = DefaultProcessModel(
            stateTransition,
            control,
            processNoise,
            initialState,
            initialStateCovariance
        )

        val measurementMatrix = Array2DRowRealMatrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 0.0)
            )
        )

        val measurementNoise = Array2DRowRealMatrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0),
                doubleArrayOf(0.0, 1.0)
            )
        )

        val measurementModel = DefaultMeasurementModel(
            measurementMatrix,
            measurementNoise
        )

        kalmanFilter = KalmanFilter(processModel, measurementModel)
    }

    fun correctLocation(measurement: RealVector): RealVector? {
        kalmanFilter?.predict()
        kalmanFilter?.correct(measurement)
        return kalmanFilter?.stateEstimationVector
    }
}