package com.d204.rumeet.ui.running.finish

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
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
import okhttp3.*
import java.io.File
import java.io.IOException


private const val TAG = "RunningFinishFragment"

@AndroidEntryPoint
class RunningFinishFragment : BaseFragment<FragmentRunningFinishBinding, RunningViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_finish

    override val viewModel: RunningViewModel by navGraphViewModels(R.id.navigation_running)

    private val args by navArgs<RunningFinishFragmentArgs>()
    private val locationList by lazy { args.locationList }
    private val runningResult by lazy { args.result }

    private var longitudeAverage: Double = 0.0
    private var latitudeAverage: Double = 0.0

    override fun initStartView() {
//        val supportMapFragment =
//            childFragmentManager.findFragmentById(R.id.iv_map) as SupportMapFragment
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
        var polyline = ""
        var polyurl = ""
        CoroutineScope(Dispatchers.Main).launch {
            locationList.forEach {
                polyline += it.latitude
                polyline += ","
                polyline += it.longitude
                polyline += "|"
            }
            polyline = polyline.removeSuffix("|")
            val client = OkHttpClient()
            val url = HttpUrl.Builder()
                .scheme("http")
                .host("119.202.203.157")
                .port(8002)
                .addQueryParameter("polyline", polyline)
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        // 요청이 성공했을 때의 처리"
                        polyurl = response.body!!.string().replace("\"", "")
                        if(polyurl.length != 0) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Glide.with(requireContext())
                                    .load(polyurl)
                                    .into(binding.ivMap)

                                viewModel.raceRecord(
                                    args.result.userId,
                                    args.result.raceId,
                                    args.result.mode,
                                    args.result.velocity,
                                    args.result.time.div(1000).toInt(),
                                    0,
                                    args.result.success,
                                    polyurl
                                )
                            }
                        }


                    }

                    override fun onFailure(call: Call, e: IOException) {
                        // 요청이 실패했을 때의 처리
                        e.printStackTrace()
                        polyurl = "https://kr.object.ncloudstorage.com/rumeet/base_profile.png"
                    }
                })

        }


    }


}