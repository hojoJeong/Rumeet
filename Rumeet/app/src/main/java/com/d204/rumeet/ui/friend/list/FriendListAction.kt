package com.d204.rumeet.ui.friend.list

import com.d204.rumeet.ui.friend.list.model.FriendListUiModel

sealed class FriendListAction {
    class SuccessFriendList(val listSize: Int) : FriendListAction()
    class SearchFriend(val userId : Int) : FriendListAction()
    class SuccessFriendInfo(val friendInfo : FriendListUiModel) : FriendListAction()
    object SortRecentlyRunFriend : FriendListAction()
    object SortRunTogetherFriend : FriendListAction()
    object NavigateAddFriend : FriendListAction()
    object SuccessSearchFriend : FriendListAction()
}