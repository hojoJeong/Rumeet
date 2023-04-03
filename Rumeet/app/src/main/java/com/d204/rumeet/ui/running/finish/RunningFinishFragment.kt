package com.d204.rumeet.ui.running.finish

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningFinishBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.running.RunningViewModel
import com.d204.rumeet.util.bitmapToFile
import com.d204.rumeet.util.roundDigit
import com.d204.rumeet.util.toMinute
import com.d204.rumeet.util.viewToBitmap
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File


private const val TAG = "RunningFinishFragment"

@AndroidEntryPoint
class RunningFinishFragment : BaseFragment<FragmentRunningFinishBinding, RunningViewModel>(),
    OnMapReadyCallback, SnapshotReadyCallback {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_finish

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    private val args by navArgs<RunningFinishFragmentArgs>()
    private val locationList by lazy { args.locationList }
    private val runningResult by lazy { args.result }

    private var longitudeAverage: Double = 0.0
    private var latitudeAverage: Double = 0.0
    private lateinit var polylineImage: File
    private lateinit var map: GoogleMap

    override fun initStartView() {
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.ly_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun initDataBinding() {

    }

    private fun initAverageLocation() {
        locationList.forEach {
            longitudeAverage += it.longitude
            latitudeAverage += it.latitude
        }
    }

    override fun initAfterBinding() {

        Log.d("running info", "initAfterBinding: ${runningResult}")
        binding.tvRunningCalorie.text = "${roundDigit(runningResult.calorie.toDouble(), 2)}"
        binding.tvRunningHeight.text = "${roundDigit(runningResult.height.toDouble(), 2)}"
        binding.tvRunningPace.text = "${roundDigit(runningResult.velocity.toDouble(), 2)}"
        binding.tvResult.text = if (runningResult.success == 0) "패배" else "승리"

        binding.tvRunningTime.text = runningResult.time.toMinute()

        binding.btnOkay.setContent("확인")
        binding.btnOkay.addClickListener {
            if (viewModel.runningRecordState.value) navigate(RunningFinishFragmentDirections.actionRunningFinishFragmentToHomeFragment())
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        initAverageLocation()

        map = p0

        Log.d(TAG, "initStartView: on map ready call back")

        val polyline = PolylineOptions().apply {
            locationList.forEach { location ->
                add(LatLng(location.latitude, location.longitude))
            }
            color(Color.RED)
            width(5f)
        }

        map.addPolyline(polyline)

        val builder = LatLngBounds.Builder().apply {
            polyline.points.forEach {
                include(it)
            }
        }.build()

        val camera = CameraUpdateFactory.newLatLngBounds(builder, 100)
        map.moveCamera(camera)
        map.snapshot(this)
    }

    override fun onSnapshotReady(p0: Bitmap?) {

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "onSnapshotReady: 사진 딜레이 시작")
            delay(3000)
            Log.d(TAG, "onSnapshotReady: 사진 딜레이 종료")
            polylineImage = bitmapToFile(p0!!, File(context?.cacheDir, "map_poly.png"))
            viewModel.raceRecord(
                args.result.userId,
                args.result.raceId,
                args.result.mode,
                args.result.velocity,
                args.result.time.div(1000).toInt(),
                0,
                args.result.success,
                polylineImage
            )
        }
    }
}