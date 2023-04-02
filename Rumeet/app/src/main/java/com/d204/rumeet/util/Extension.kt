package com.d204.rumeet.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.DecimalFormat


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
fun getAbsolutePath(path: Uri?, context: Context) : String{
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

fun RecyclerView.scrollToBottom() {
    // scroll to last item to get the view of last item
    val layoutManager = layoutManager as LinearLayoutManager?
    val lastItemPosition = adapter?.itemCount?.minus(1) ?: 0
    layoutManager!!.scrollToPositionWithOffset(lastItemPosition, 0)
    post { // then scroll to specific offset
        val target: View? = layoutManager.findViewByPosition(lastItemPosition)
        if (target != null) {
            val offset: Int = measuredHeight - target.measuredHeight
            layoutManager.scrollToPositionWithOffset(lastItemPosition, offset)
        }
    }
}

fun floatTo2f(data : Float) : String{
    val df = DecimalFormat("##0.00")
    return df.format(data)
}

fun jsonToString(json : Any): String? = Gson().toJson(json)

fun getCalorie(gender : Int, age : Int, weight : Float, time : Long) : String{
    val minute = (time/1000/60)
    val calcAge = roundDigit(age.toDouble().times(0.2017), 2)
    // 파운드 기준
    val calcWeight = roundDigit(weight.times(2.20462).times(0.09036), 2)
    val calcHeart = roundDigit(120.times(0.6309), 2)

    val firstCalc : Double = roundDigit((calcAge + calcWeight + calcHeart - 55.0969) * minute.toDouble(), 2)
    return floatTo2f(roundDigit(firstCalc.div(4.184) ,2).toFloat())
}

fun roundDigit(number : Double, digits : Int): Double {
    return Math.round(number * Math.pow(10.0, digits.toDouble())) / Math.pow(10.0, digits.toDouble())
}