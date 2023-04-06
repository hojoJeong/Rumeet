package com.d204.rumeet.ui.mypage

sealed class MyPageAction {
    object RunningRecord : MyPageAction()
    object MatchingHistory : MyPageAction()
    object FriendList : MyPageAction()
    object BadgeList : MyPageAction()
    object EditProfile : MyPageAction()
    object Setting : MyPageAction()
}
