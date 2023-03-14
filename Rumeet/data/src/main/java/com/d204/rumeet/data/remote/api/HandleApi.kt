package com.d204.rumeet.data.remote.api

import com.d204.rumeet.domain.NetworkResult


internal inline fun <T> handleApi(transform: () -> T) = try {
    NetworkResult.Success(transform.invoke())
}catch (e : Exception){

}