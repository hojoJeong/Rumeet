package com.d204.rumeet.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import com.d204.rumeet.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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

fun Context.getColorWithNoTheme(colorRes : Int) = ResourcesCompat.getColor(resources, colorRes, null)

@SuppressLint("Recycle")
fun Context.getAbsolutePath(path : Uri?, context : Context) : String{
    val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
    val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
    val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    c?.moveToFirst()

    val result = c?.getString(index!!)

    return result!!
}

fun Context.getMultipartData(imagePath : Uri?) : MultipartBody.Part{
    val file = File(getAbsolutePath(imagePath, this))
    val mediaType = "image/png".toMediaTypeOrNull()
    val requestFile = file.asRequestBody(mediaType)
    return MultipartBody.Part.createFormData("profile_img",file.name, requestFile)
}