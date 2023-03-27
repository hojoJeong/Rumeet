package com.d204.rumeet.ui.friend_list

import com.d204.rumeet.ui.friend_list.model.FriendInfoModel

sealed class FriendListAction {
    class SuccessFriendList(val listSize: Int) : FriendListAction()
    class SearchFriend(val userId : Int) : FriendListAction()
    class ShowFriendInfoDialog(val friendInfo : FriendInfoModel) : FriendListAction()
    object SortRecentlyRunFriend : FriendListAction()
    object SortRunTogetherFriend : FriendListAction()
}