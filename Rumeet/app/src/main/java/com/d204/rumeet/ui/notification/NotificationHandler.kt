package com.d204.rumeet.ui.notification

interface NotificationHandler {
    fun onClick(friendId: Int, myId: Int, accept: Boolean)
}