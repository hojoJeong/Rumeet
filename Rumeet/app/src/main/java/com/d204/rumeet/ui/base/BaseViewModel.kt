package com.d204.rumeet.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d204.rumeet.data.remote.dto.HaveNotJwtTokenException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {

    private val _errorEvent: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    private val _loadingEvent: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0, extraBufferCapacity = 10)
    val loadingEvent: SharedFlow<Boolean> = _loadingEvent.asSharedFlow()

    private val errorHandler = CoroutineExceptionHandler { CoroutineContext, throwable ->
        viewModelScope.launch(CoroutineContext) {
            _errorEvent.emit(throwable)
        }
    }

    private val _needLoginEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val needLoginEvent: SharedFlow<Boolean> = _needLoginEvent

    /**
     * api 호출시 필요함, 실패하면 해당 로직으로 이동
     * 만약 로그인 오류라면 로그인 액티비티로 이동
     * */
    fun catchError(e: Throwable?) {
        viewModelScope.launch(errorHandler) {
            e?.let { exception ->
                when (exception) {
                    is HaveNotJwtTokenException -> {
                        _needLoginEvent.emit(true)
                    }
                    else -> _errorEvent.emit(exception)
                }
            }
        }
    }

    fun showLoading() {
        baseViewModelScope.launch {
            _loadingEvent.emit(true)
        }
    }

    fun dismissLoading() {
        baseViewModelScope.launch {
            _loadingEvent.emit(false)
        }
    }

    protected val baseViewModelScope: CoroutineScope = viewModelScope + errorHandler
}
