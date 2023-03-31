package com.d204.rumeet.util

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.d204.rumeet.domain.model.chatting.ChattingModel
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toMinute() : String{
    val simpleDateFormat = SimpleDateFormat("mm:ss",Locale.KOREA)
    return simpleDateFormat.format(this)
}

fun Context.getLocation(): Location? {
    val locationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return null
    }
    return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
}
