package com.d204.rumeet.util

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
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
    return simpleDateFormat.format(this * 1000)
}

fun Long.toMinuteTime() : String{
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


fun Long.toDate() : String{
    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    return simpleDateFormat.format(this)
}

fun String.toDate() : Long{
    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    return simpleDateFormat.parse(this).time
}

fun Int.toCount() : String {
    val times = "${this.toString()}회"
    val builder = SpannableStringBuilder(times).apply {
        setSpan(
            RelativeSizeSpan(0.7f), times.length-1, times.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return builder.toString()
}

fun Int.toDistance(): String {
    val distance = "${this.toString()}km"
    val builder = SpannableStringBuilder(distance).apply {
        setSpan(
            RelativeSizeSpan(0.7f), distance.length-2, distance.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return builder.toString()
}


fun Int.toRecord() : String {
    val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.KOREA)
    return simpleDateFormat.format(this*1000)
}