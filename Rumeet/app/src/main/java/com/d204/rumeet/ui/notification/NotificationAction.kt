package com.d204.rumeet.ui.notification

import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel

sealed class NotificationAction {
    class FriendRequest(val friendList: List<NotificationListDomainModel>): NotificationAction()
    class RunningRequest(val runningList: List<RunningRequestDomainModel>): NotificationAction()
    class AcceptFriendRequest(val myId: Int, val friendId: Int): NotificationAction()
    class DenyFriendRequest(val myId: Int, val friendId: Int): NotificationAction()
    class AcceptRunningRequest(val raceId: Int, val index: Int): NotificationAction()
    class DenyRunningRequest(val raceId: Int, val index: Int): NotificationAction()
}