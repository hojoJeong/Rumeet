package com.d204.rumeet.ui.friend_list

import com.d204.rumeet.ui.friend_list.model.FriendListModel

sealed class FriendListAction {
    class SuccessFriendList(val listSize: Int) : FriendListAction()
    class SearchFriend(val userId : Int) : FriendListAction()
    class SuccessFriendInfo(val friendInfo : FriendListModel) : FriendListAction()
    object SortRecentlyRunFriend : FriendListAction()
    object SortRunTogetherFriend : FriendListAction()
}