package com.d204.rumeet.ui.home

import android.content.ContentValues
import android.util.Log
import com.d204.rumeet.domain.usecase.user.GetUserIdUseCase
import com.d204.rumeet.domain.usecase.user.RegistFcmTokenUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val registFcmTokenUseCase: RegistFcmTokenUseCase
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

    fun getHomeData() {
        baseViewModelScope.launch {
            //TODO(서버 통신)
            try {
                val response = "배달전문 박정은"
                _userName.value = UiState.Success(response)
            } catch (e: Exception) {
                _userName.value = UiState.Error(e.cause)
            }
        }
    }

    fun getBestRecordListForHome() {
        baseViewModelScope.launch {
            try {
                //TODO(서버 통신, 초기 상태로 임시 처리, 체력데이터가 있다면 data layer mapper에서 형식에 맞게 변환해서 도메인 레이어로 가져올 것)
                val response = emptyList<BestRecordUiModel>()
                _bestRecord.value = UiState.Success(response)
            } catch (e: Exception) {
                _bestRecord.value = UiState.Error(e.cause)
            }
        }
    }

    fun getBadgeListForHome() {
        baseViewModelScope.launch {
            try {
                val response = emptyList<String>()
                _badgeList.value = UiState.Success(response)
            } catch (e: Exception) {
                _badgeList.value = UiState.Error(e.cause)
            }
        }
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
            Log.d(ContentValues.TAG, "initFcm: userId: ${userId.value.successOrNull()}, FCM Token : $token")
            baseViewModelScope.launch {
                registFcmTokenUseCase(userId.value.successOrNull() ?: -1, token)
            }
        }
    }
}