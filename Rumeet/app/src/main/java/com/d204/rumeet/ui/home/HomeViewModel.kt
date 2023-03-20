package com.d204.rumeet.ui.home

import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    private val _userName = MutableSharedFlow<String>()
    val userName: SharedFlow<String>
        get() = _userName.asSharedFlow()

    private val _bestRecord = MutableSharedFlow<List<BestRecordUiModel>>()
    val bestRecord: SharedFlow<List<BestRecordUiModel>>
        get() = _bestRecord.asSharedFlow()

    private val _badgeList = MutableSharedFlow<List<String>>()
    val badgeList: SharedFlow<List<String>>
        get() = _badgeList.asSharedFlow()

    private val _recommendFriendList = MutableSharedFlow<List<RecommendFriendUiModel>>()
    val recommendFriendList: SharedFlow<List<RecommendFriendUiModel>>
        get() = _recommendFriendList.asSharedFlow()

    suspend fun getHomeData() {
        baseViewModelScope.launch {
            // 친구 추천은 갱신이 필요하므로 홈 전체 데이터를 갱신할 필요가 없음. 따로 데이터바인딩 해주는 게 맞는지?
//            val homeUiData = HomeUiModel(
//                "배달전문 박정은",
//                emptyList(),    //UseCase 호출
//                emptyList(),
//                emptyList()
//            )

            _userName.emit("배달전문 박정은")
            _bestRecord.emit(emptyList())
            _badgeList.emit(emptyList())
            _recommendFriendList.emit(emptyList())

        }
    }
}