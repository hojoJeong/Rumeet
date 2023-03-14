package com.d204.rumeet.domain

sealed class NetworkResult<out R> {
    data class Success<out T>(val data : T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val NetworkResult<*>.succeeded
    get() = this is NetworkResult.Success

fun <T> NetworkResult<T>.successOr(fallback : T) : T{
    return (this as? NetworkResult.Success<T>)?.data ?: fallback
}