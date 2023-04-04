package com.d204.rumeet.ui.notification

interface NotificationHandler {
    fun onClickFriend(friendId: Int, myId: Int, accept: Boolean)
    fun onClickRunning(raceId: Int, accept: Boolean)
}