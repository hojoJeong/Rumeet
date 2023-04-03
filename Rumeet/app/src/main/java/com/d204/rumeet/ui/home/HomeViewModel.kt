package com.d204.rumeet.ui.home

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.domain.model.friend.FriendRecommendDomainModel
import com.d204.rumeet.domain.model.user.HomeBadgeDomainModel
import com.d204.rumeet.domain.model.user.HomeDataDomainModel
import com.d204.rumeet.domain.model.user.HomeRecordDomainModel
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.user.*
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import com.d204.rumeet.ui.mypage.model.BadgeDetailUiModel
import com.d204.rumeet.util.toRecord
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val registFcmTokenUseCase: RegistFcmTokenUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getFriendRecommendListUseCase: GetFriendRecommendListUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getFriendDetailInfoUseCase: GetFriendDetailInfoUseCase
) : BaseViewModel() {
    private val _userId: MutableStateFlow<UiState<Int>> = MutableStateFlow(UiState.Loading)
    val userId: StateFlow<UiState<Int>>
        get() = _userId

    private val _userName: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Loading)
    val userName: StateFlow<UiState<String>>
        get() = _userName.asStateFlow()

    private val _homeRecord: MutableStateFlow<UiState<List<BestRecordUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val homeRecord: StateFlow<UiState<List<BestRecordUiModel>>>
        get() = _homeRecord.asStateFlow()

    private val _badgeList: MutableStateFlow<UiState<List<String>>> =
        MutableStateFlow(UiState.Loading)
    val badgeList: StateFlow<UiState<List<String>>>
        get() = _badgeList.asStateFlow()

    private val _recommendFriendList: MutableStateFlow<UiState<List<RecommendFriendUiModel>>> =
        MutableStateFlow(UiState.Loading)
    val recommendFriendList: StateFlow<UiState<List<RecommendFriendUiModel>>>
        get() = _recommendFriendList.asStateFlow()

    private val _homeResponse: MutableStateFlow<UiState<HomeDataDomainModel>> =
        MutableStateFlow(UiState.Loading)
    val homeResponse: StateFlow<UiState<HomeDataDomainModel>>
        get() = _homeResponse.asStateFlow()

    private val _friendRecommendList: MutableStateFlow<UiState<List<FriendRecommendDomainModel>>> = MutableStateFlow(UiState.Loading)
    val friendRecommendList: StateFlow<UiState<List<FriendRecommendDomainModel>>>
        get() = _friendRecommendList

    private val _friendDetailInfo: MutableStateFlow<UiState<FriendInfoDomainModel>> = MutableStateFlow(UiState.Loading)
    val friendDetailInfo: StateFlow<UiState<FriendInfoDomainModel>> get() = _friendDetailInfo.asStateFlow()

    fun getUserIdByUseCase() {
        baseViewModelScope.launch {
            try {
                val response = getUserIdUseCase()
                Log.d(TAG, "getUserIdByUseCase: $response")
                _userId.value = UiState.Success(response)
            } catch (e: Exception) {
                _userId.value = UiState.Error(e.cause)
                catchError(e)
            }
        }
    }

    fun getHomeData() {
        baseViewModelScope.launch {
            getHomeDataUseCase(userId.value.successOrNull()!!)
                .onSuccess { response ->
                    _userName.value = UiState.Success(response.record.nickname.toString())
                    _homeResponse.value = UiState.Success(response)
                    setHomeRecord(response.record)
                    //TODO 친구 추천 서버 통신
                }
                .onError {
                    catchError(it)
                }
        }
    }

    private fun setHomeRecord(record: HomeRecordDomainModel) {
        try {
            val totalCount = "${record.totalCount}회"
            val totalDistance = "${record.totalKm}km"
            var pace = record.averagePace.toString()

            val myRecord = listOf(
                BestRecordUiModel(totalCount, "누적 횟수"),
                BestRecordUiModel(totalDistance, "누적 거리"),
                BestRecordUiModel(pace, "평균 페이스")
            )
            _homeRecord.value = UiState.Success(myRecord)
            Log.d(TAG, "setHomeRecord 내 기록: ${homeRecord.value.successOrNull()}")

        } catch (e: Exception) {
            _homeRecord.value = UiState.Error(e.cause)
        }
    }

    fun setBadgeList(list: List<String>) {
        Log.d(TAG, "setBadgeList: $list")
        _badgeList.value = UiState.Success(list)
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

    fun getFriendRecommendList() {
        baseViewModelScope.launch {
            showLoading()
            getFriendRecommendListUseCase(userId.value.successOrNull() ?: -1)
                .onSuccess {
                    dismissLoading()
                    _friendRecommendList.value = UiState.Success(it)
                }
                .onError {
                    dismissLoading()
                }
        }
    }

    fun getFriendInfo(userId: Int){
        baseViewModelScope.launch {
            showLoading()
            getFriendDetailInfoUseCase(userId)
                .onSuccess {
                    dismissLoading()
                    _friendDetailInfo.value = UiState.Success(it)
                }
                .onError {
                    dismissLoading()
                }
        }
    }
}