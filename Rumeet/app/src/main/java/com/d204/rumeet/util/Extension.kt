package com.d204.rumeet.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.d204.rumeet.R

fun Context.startActivityAfterClearBackStack(classType: Class<out Activity>) {
    val intent = Intent(this, classType).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    this.startActivity(intent)
}

fun Button.setTextColorWithNoTheme(colorRes: Int) {
    setTextColor(ResourcesCompat.getColor(resources, colorRes, null))
}

fun TextView.setTextColorWithNoTheme(colorRes : Int){
    setTextColor(ResourcesCompat.getColor(resources, colorRes, null))
}