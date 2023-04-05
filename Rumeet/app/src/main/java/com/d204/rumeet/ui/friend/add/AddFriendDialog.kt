package com.d204.rumeet.ui.friend.add

import android.view.View
import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogFriendAddBinding
import com.d204.rumeet.ui.base.BaseDialogFragment
import com.d204.rumeet.ui.friend.UserDialogModel
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.toUserDialogModel

class AddFriendDialog : BaseDialogFragment<DialogFriendAddBinding>(R.layout.dialog_friend_add) {
    override val layoutResourceId: Int
        get() = R.layout.dialog_friend_add

    private lateinit var userData: UserDialogModel
    private lateinit var requestFriend: (Int) -> Unit

    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        binding.contentUserInfo.tvBestRecord.text = "누적 기록"
        binding.contentUserInfo.tvAveragePace.visibility = View.GONE
        binding.contentUserInfo.tvCalorieTitle.visibility = View.GONE
        with(binding.contentUserInfo) {
            userDialogModel = userData
            tvDistance.text = userData.totalKm
            tvTime.text = userData.totalTime
            tvPace.text = userData.pace
        }
        binding.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnRequestFriend.setOnClickListener {
            requestFriend.invoke(userData.id)
            dismissAllowingStateLoss()
        }
    }

    fun setUserData(userData: UserListUiModel) {
        this.userData = userData.toUserDialogModel()
    }

    fun setRequestButtonEvent(event: (Int) -> Unit) {
        requestFriend = event
    }
}