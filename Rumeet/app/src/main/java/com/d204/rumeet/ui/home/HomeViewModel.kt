package com.d204.rumeet.ui.home

import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.UiState
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

    fun getUserNameForWelcomeMessage() {
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

    fun getBestRecordList() {
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

    fun getBadgeList() {
        baseViewModelScope.launch {
            try {
                val list = mutableListOf<String>()
                list.add("http://k.kakaocdn.net/dn/w6Z90/btr2giRz3IZ/VY50KEe8k967kjif2z728k/img_110x110.jpg")
                list.add("http://k.kakaocdn.net/dn/w6Z90/btr2giRz3IZ/VY50KEe8k967kjif2z728k/img_110x110.jpg")
                list.add("http://k.kakaocdn.net/dn/w6Z90/btr2giRz3IZ/VY50KEe8k967kjif2z728k/img_110x110.jpg")

                _badgeList.value = UiState.Success(list)
            } catch (e: Exception) {
                _badgeList.value = UiState.Error(e.cause)
            }
        }
    }

    fun getRecommendFriendList() {
        baseViewModelScope.launch {
            try {
                val response = emptyList<RecommendFriendUiModel>()
                _recommendFriendList.value = UiState.Success(response)
            } catch (e: Exception) {
                _recommendFriendList.value = UiState.Error(e.cause)
            }
        }
    }
}