package com.d204.rumeet.ui.running.matching.model

data class RunningMatchingRequestModel(
    val userId : Int,
    /**
    0 : 개인모드 & 1km
    1 : 개인모드 & 2km
    2 : 개인모드 & 3km
    3 : 개인모드 & 5km
    =============
    4 : 경쟁모드 & 1km
    5 : 경쟁모드 & 2km
    6 : 경쟁모드 & 3km
    7 : 경쟁모드 & 5km
    =============
    8 : 협동모드 & 1km
    9 : 협동모드 & 2km
    10 : 협동모드 & 3km
    11 : 협동모드 & 5km
     * */
    val mode : Int
)
