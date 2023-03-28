package com.d204.rumeet.ui.friend.list

import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogFriendInfoBinding
import com.d204.rumeet.ui.base.BaseDialogFragment
import com.d204.rumeet.ui.friend.UserDialogModel
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel
import com.d204.rumeet.ui.friend.toUserDialogModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendInfoDialog : BaseDialogFragment<DialogFriendInfoBinding>(R.layout.dialog_friend_info){
    override val layoutResourceId: Int
        get() = R.layout.dialog_friend_info

    private lateinit var chattingClickListener: (Int) -> Unit
    private lateinit var userDialogModel: UserDialogModel

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.contentUserInfo
        binding.btnChatting.setOnClickListener {
            chattingClickListener.invoke(userDialogModel.id)
            dismissAllowingStateLoss()
        }
        binding.btnOkay.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    fun addChattingButtonClickListener(event : (Int) -> Unit){
        chattingClickListener = event
    }

    fun initFriendInfo(data : FriendListUiModel){
        userDialogModel = data.toUserDialogModel()
    }
}