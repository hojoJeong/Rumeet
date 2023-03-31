package com.d204.rumeet.ui.home

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.d204.rumeet.R
import com.d204.rumeet.domain.model.user.HomeBadgeDomainModel
import com.d204.rumeet.domain.model.user.HomeDataDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.GetHomeDataUseCase
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.domain.usecase.user.RegistFcmTokenUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import com.d204.rumeet.util.toCount
import com.d204.rumeet.util.toDate
import com.d204.rumeet.util.toDistance
import com.d204.rumeet.util.toRecord
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val registFcmTokenUseCase: RegistFcmTokenUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase
) : BaseViewModel() {
    private val _userId: MutableStateFlow<UiState<Int>> = MutableStateFlow(UiState.Loading)
    val userId: StateFlow<UiState<Int>>
        get() = _userId

    private val _userName: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Loading)
    val userName: StateFlow<UiState<String>>
        get() = _userName.asStateFlow()

    private val _bestRecord: MutableStateFlow<UiState<List<BestRecordUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val bestRecord: StateFlow<UiState<List<BestRecordUiModel>>>
        get() = _bestRecord.asStateFlow()

    private val _badgeList: MutableStateFlow<UiState<List<String>>> =
        MutableStateFlow(UiState.Loading)
    val badgeList: StateFlow<UiState<List<String>>>
        get() = _badgeList.asStateFlow()

    private val _recommendFriendList: MutableStateFlow<UiState<List<RecommendFriendUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val recommendFriendList: StateFlow<UiState<List<RecommendFriendUiModel>>>
        get() = _recommendFriendList.asStateFlow()

    fun getUserIdByUseCase() {
        baseViewModelScope.launch {
            try {
                val response = getUserIdUseCase()
                _userId.value = UiState.Success(response)
            } catch (e: Exception) {
                _userId.value = UiState.Error(e.cause)
                catchError(e)
            }
        }
    }

    fun getHomeData(context: Context) {
        baseViewModelScope.launch {
            showLoading()
            getHomeDataUseCase(userId.value.successOrNull() ?: -1)
                .onSuccess { response ->
                    dismissLoading()
                    _userName.value = UiState.Success(response.record.nickname ?: "")

                    setHomeRecord(response)

                    val badgeList = listOf(
                        context.resources.getStringArray(R.array.url_badge)[context.resources.getStringArray(R.array.code_badge).indexOf(response.badge[0].code.toString())],
                        context.resources.getStringArray(R.array.url_badge)[context.resources.getStringArray(R.array.code_badge).indexOf(response.badge[1].code.toString())],
                        context.resources.getStringArray(R.array.url_badge)[context.resources.getStringArray(R.array.code_badge).indexOf(response.badge[2].code.toString())],
                        )
                    _badgeList.value = UiState.Success(badgeList)
                }
                .onError {
                    dismissLoading()
                    catchError(it)
                }
        }
    }

    private fun setHomeRecord(response: HomeDataDomainModel){
        val totalCount = response.record.totalCount?.toCount()
        val totalDistance = response.record.totalKm?.toDistance()
        val pace = response.record.averagePace?.toLong()?.toRecord()
        val myRecord = listOf(
            BestRecordUiModel(totalCount ?: "", "누적 횟수"),
            BestRecordUiModel(totalDistance ?: "", "누적 거리"),
            BestRecordUiModel(pace?:"", "평균 페이스")
        )
        _bestRecord.value = UiState.Success(myRecord)
    }

    fun getRecommendFriendListForHome() {
        baseViewModelScope.launch {
            try {
                val response = emptyList<RecommendFriendUiModel>()
                _recommendFriendList.value = UiState.Success(response)
            } catch (e: Exception) {
                _recommendFriendList.value = UiState.Error(e.cause)
            }
        }
    }

    fun registFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d(
                ContentValues.TAG,
                "initFcm: userId: ${userId.value.successOrNull()}, FCM Token : $token"
            )
            baseViewModelScope.launch {
                registFcmTokenUseCase(userId.value.successOrNull() ?: -1, token)
            }
        }
    }
}