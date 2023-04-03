package com.d204.rumeet.ui.running.finish

import android.graphics.Color
import android.util.Log
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File


private const val TAG = "RunningFinishFragment"
@AndroidEntryPoint
class RunningFinishFragment : BaseFragment<FragmentRunningFinishBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_finish

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    private val args by navArgs<RunningFinishFragmentArgs>()
    private val locationList by lazy { args.locationList }
    private val runningResult by lazy { args.result }

    private var longitudeAverage : Double = 0.0
    private var latitudeAverage : Double = 0.0
    private lateinit var polylineImage: File
    private lateinit var map : SupportMapFragment

    override fun initStartView() {
        map = childFragmentManager.findFragmentById(R.id.ly_map) as SupportMapFragment
        map.getMapAsync {
            Log.d(TAG, "initStartView: map async")
            OnMapReadyCallback {
                Log.d(TAG, "initStartView: on map ready call back")
                val location = LatLng(latitudeAverage, longitudeAverage)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(location,10f))

                val polyline = PolylineOptions().apply {
                    locationList.forEach { location ->
                        add(LatLng(location.latitude, location.longitude))
                    }
                    color(Color.RED)
                }
                polyline.width(10f)
                it.addPolyline(polyline)

                captureMapView()
            }
        }
        initAverageLocation()
    }

    override fun initDataBinding() {

    }

    private fun initAverageLocation(){
        locationList.forEach {
            longitudeAverage += it.longitude
            latitudeAverage += it.latitude
        }
        longitudeAverage /= locationList.size
        latitudeAverage /= locationList.size
    }

    private fun captureMapView(){

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("update", "onCurrentLocationUpdate: start loading...")
            delay(10000)

            withContext(Dispatchers.IO){
    //                Log.d("map size", "${binding.lyMap.width}, ${binding.lyMap.height} ")
    //                val bitmap = viewToBitmap(binding.lyMap)
    //                polylineImage = bitmapToFile(bitmap, File(context?.cacheDir, "map_poly.png"))
            }


            withContext(Dispatchers.IO){
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

    override fun initAfterBinding() {

        Log.d("running info", "initAfterBinding: ${runningResult}")
        binding.tvRunningCalorie.text = "${roundDigit(runningResult.calorie.toDouble(), 2)}"
        binding.tvRunningHeight.text = "${roundDigit(runningResult.height.toDouble(), 2)}"
        binding.tvRunningPace.text = "${roundDigit(runningResult.velocity.toDouble(),2)}"
        binding.tvResult.text = if(runningResult.success == 0) "패배" else "승리"

        binding.tvRunningTime.text = runningResult.time.toMinute()

        binding.btnOkay.setContent("확인")
        binding.btnOkay.addClickListener{
            if(viewModel.runningRecordState.value) navigate(RunningFinishFragmentDirections.actionRunningFinishFragmentToHomeFragment())
        }
    }
}