package com.d204.rumeet.data.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun getProfileMultipartData(file : File?): MultipartBody.Part? {
    return file?.let {
        val mediaType = "image/*".toMediaTypeOrNull()
        val requestFile = file.asRequestBody(mediaType)
        MultipartBody.Part.createFormData("profile_img", file.name, requestFile)
    }
}

fun getPolylineMultipartData(file : File?) : MultipartBody.Part?{
    return file?.let {
        val mediaType = "image/*".toMediaTypeOrNull()
        val requestFile = file.asRequestBody(mediaType)
        MultipartBody.Part.createFormData("polyline", file.name, requestFile)
    }
}