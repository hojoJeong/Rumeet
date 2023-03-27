package com.d204.rumeet.util

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toMinute() : String{
    val simpleDateFormat = SimpleDateFormat("mm:ss",Locale.KOREA)
    return simpleDateFormat.format(this)
}