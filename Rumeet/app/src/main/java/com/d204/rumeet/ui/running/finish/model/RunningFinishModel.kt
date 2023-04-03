package com.d204.rumeet.ui.running.finish.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningFinishModel(
    // 1 = 승리, 0 = 패배
    val success : Int,
    // 평균 속도
    val velocity : Float,
    // 총 칼로리
    val calorie : Float,
    // 평균 고도
    val height : Float,

    val userId : Int,
    val raceId : Int,
    val mode : Int,
    val time : Long,
) : Parcelable
