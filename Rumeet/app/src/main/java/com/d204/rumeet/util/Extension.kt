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
import com.d204.rumeet.ui.find_account.FindAccountAction
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

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
fun getAbsolutePath(path : Uri?, context : Context) : String{
    val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
    val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
    val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    c?.moveToFirst()

    val result = c?.getString(index!!)

    return result!!
}

fun hashingSHA256(input : String) : String{
    val md = MessageDigest.getInstance("SHA256")
    val hash = md.digest(input.toByteArray(StandardCharsets.UTF_8))
    return bytesToHex(hash)
}

fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder(2 * hash.size)
    for (b in hash) {
        val hex = Integer.toHexString(0xff and b.toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }
    return hexString.toString()
}

fun checkEmailValidate(email : String) : Boolean{
    val pattern = android.util.Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
}

fun Int.toMode() : String{
    var mode = ""
    when(this){
        0 -> mode = "싱글 1km"
        1 -> mode = "싱글 2km"
        2 -> mode = "싱글 3km"
        3 -> mode = "싱글 5km"
        4 -> mode = "경쟁 1km"
        5 -> mode = "경쟁 2km"
        6 -> mode = "경쟁 3km"
        7 -> mode = "경쟁 5km"
        8 -> mode = "협동 1km"
        9 -> mode = "협동 2km"
        10 -> mode = "협동 3km"
        11 -> mode = "협동 5km"
    }
    return mode
}

fun Int.toSuccess(): String{
    if(this == 0) return "패배"
    else return "승리"
}