package com.d204.rumeet.ui.friend.list

import android.util.Log
import android.view.View
import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogFriendInfoBinding
import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.ui.base.BaseDialogFragment
import com.d204.rumeet.ui.friend.UserDialogModel
import com.d204.rumeet.ui.friend.add.AddFriendViewModel
import com.d204.rumeet.ui.friend.toUserDialogModel
import com.d204.rumeet.util.toDistance
import com.d204.rumeet.util.toRecord
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendInfoDialog : BaseDialogFragment<DialogFriendInfoBinding>(R.layout.dialog_friend_info) {
    override val layoutResourceId: Int
        get() = R.layout.dialog_friend_info

    private lateinit var chattingClickListener: (Int) -> Unit
    private lateinit var okBtnClickListerner: () -> Unit
    private lateinit var userDialogInfo: UserDialogModel
    lateinit var viewInfo: String
    lateinit var randomFriendInfo: FriendInfoDomainModel
    lateinit var friendViewModel: AddFriendViewModel
    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        if (viewInfo == "random_friend") {
            setRandomFriendInfoDetail()
        } else {
            with(binding.contentUserInfo) {
                userDialogModel = userDialogInfo
                tvDistance.text = userDialogInfo.totalKm
                tvTime.text = userDialogInfo.totalTime
                tvPace.text = userDialogInfo.pace
            }
            binding.btnOkay.setContent("러닝 신청")
            binding.btnChatting.setOnClickListener {
                chattingClickListener.invoke(userDialogInfo.id)
                dismissAllowingStateLoss()
            }
            binding.btnOkay.addClickListener {
                Log.d("TAG", "initAfterBinding: ")
                okBtnClickListerner.invoke()
                dismissAllowingStateLoss()
            }
        }

    }

    fun addOkBtnClickListener(event: () -> Unit){
        Log.d("TAG", "addOkBtnClickListener: ")
        okBtnClickListerner = event
    }

    fun addChattingButtonClickListener(event: (Int) -> Unit) {
        chattingClickListener = event
    }

    fun initFriendInfo(data: FriendInfoDomainModel) {
        userDialogInfo = data.toUserDialogModel()
    }

    private fun setRandomFriendInfoDetail() {
        with(binding.contentUserInfo) {
            url = randomFriendInfo.profileImg
            ivProfileImg.visibility = View.VISIBLE
            tvPace.text = randomFriendInfo.pace.toRecord()
            tvTime.text = randomFriendInfo.totalTime.toRecord()
            tvDistance.text = randomFriendInfo.totalKm.toDistance()
        }
        binding.btnChatting.visibility = View.GONE

        binding.btnOkay.setContent("친구 요청")
        binding.btnOkay.addClickListener {
            friendViewModel.requestFriend(randomFriendInfo.id)
            dismissAllowingStateLoss()
        }
        binding.btnDialogCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

}