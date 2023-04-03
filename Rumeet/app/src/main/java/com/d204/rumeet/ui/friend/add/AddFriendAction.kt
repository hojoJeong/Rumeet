package com.d204.rumeet.ui.friend.add

import com.d204.rumeet.ui.friend.add.model.UserListUiModel

sealed class AddFriendAction {
    class SuccessRequestFriendList(val listSize: Int) : AddFriendAction()
    class ShowUserInfoDialog(val data: UserListUiModel) : AddFriendAction()
    object AlreadyRequestFriend : AddFriendAction()
    object AlreadyFriend : AddFriendAction()
    object SuccessRequestFriend : AddFriendAction()
}