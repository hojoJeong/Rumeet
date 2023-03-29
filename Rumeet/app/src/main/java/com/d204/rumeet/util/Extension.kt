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

fun resizeImage(imageFile: File, targetWidth: Int, targetHeight: Int): Bitmap {
    // 이미지 파일 로드
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true // 이미지 정보만 가져오기 위해 inJustDecodeBounds 옵션을 true로 설정
    }
    BitmapFactory.decodeFile(imageFile.path, options)

    // 이미지 리사이징
    val width = options.outWidth
    val height = options.outHeight
    var scaleFactor = 1
    if (width > targetWidth || height > targetHeight) {
        val widthRatio = width.toFloat() / targetWidth.toFloat()
        val heightRatio = height.toFloat() / targetHeight.toFloat()
        scaleFactor = Math.min(widthRatio, heightRatio).toInt()
    }
    val scaledOptions = BitmapFactory.Options().apply {
        inSampleSize = scaleFactor // 이미지 리사이징 옵션 설정
    }

    return BitmapFactory.decodeFile(imageFile.path, scaledOptions)
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