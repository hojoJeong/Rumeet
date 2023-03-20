package com.d204.rumeet.ui.home

import android.util.Log
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
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

    fun getUserName() {
        baseViewModelScope.launch {
            //TODO(서버 통신)
            try {
                val response = "배달전문 박정은"
                _userName.emit(UiState.Success(response))
            } catch (e: Exception) {
                _userName.emit(UiState.Error(e.cause))
            }
        }
    }

    fun getBestRecordList() {
        baseViewModelScope.launch {
            try {
                //TODO(서버 통신, 초기 상태로 임시 처리, 체력데이터가 있다면 data layer mapper에서 형식에 맞게 변환해서 도메인 레이어로 가져올 것)
                val response = emptyList<BestRecordUiModel>()
                _bestRecord.emit(UiState.Success(response))
            } catch (e: Exception) {
                _bestRecord.emit(UiState.Error(e.cause))
            }
        }
    }

    fun getBadgeList() {
        baseViewModelScope.launch {
            try {
                val response = emptyList<String>()
                _badgeList.emit(UiState.Success(response))
            } catch (e: Exception) {
                _badgeList.emit(UiState.Error(e.cause))
            }
        }
    }

    fun getRecommendFriendList() {
        baseViewModelScope.launch {
            try {
                val response = emptyList<RecommendFriendUiModel>()
                _recommendFriendList.emit(UiState.Success(response))
            } catch (e: Exception) {
                _recommendFriendList.emit(UiState.Error(e.cause))
            }
        }
    }
}