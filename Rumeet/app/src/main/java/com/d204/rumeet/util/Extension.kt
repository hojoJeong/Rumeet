package com.d204.rumeet.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext

fun Context.startActivity(classType: Class<out Activity>) {
    val intent = Intent(this, classType).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    this.startActivity(intent)
}