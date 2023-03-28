package com.d204.rumeet.ui.friend_list

import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogFriendInfoBinding
import com.d204.rumeet.ui.base.BaseDialogFragment
import com.d204.rumeet.ui.friend_list.model.FriendListModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendInfoDialog : BaseDialogFragment<DialogFriendInfoBinding>(R.layout.dialog_friend_info){
    override val layoutResourceId: Int
        get() = R.layout.dialog_friend_info

    private lateinit var chattingClickListener: (Int) -> Unit

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.btnChatting.setOnClickListener {
            chattingClickListener.invoke(binding.friendBindData?.userId!!)
            dismissAllowingStateLoss()
        }
        binding.btnOkay.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    fun addChattingButtonClickListener(event : (Int) -> Unit){
        chattingClickListener = event
    }

    fun initFriendInfo(data : FriendListModel){
        binding.friendBindData = data
    }
}