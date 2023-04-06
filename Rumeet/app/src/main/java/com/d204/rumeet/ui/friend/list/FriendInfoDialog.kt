package com.d204.rumeet.ui.friend.list

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
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
    lateinit var friendInfo: FriendInfoDomainModel
    lateinit var friendViewModel: AddFriendViewModel
    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        if (viewInfo == "friend") {
            setFriendInfoDetail()
        } else {
            with(binding.contentUserInfo) {
                userDialogModel = userDialogInfo
                tvAveragePace.visibility = View.GONE
                tvDistance.text = userDialogInfo.totalKm
                tvTime.text = userDialogInfo.totalTime
                tvPace.text = userDialogInfo.pace
            }
            binding.btnOkay.setContent("러닝 신청")
            binding.btnChatting.setOnClickListener {
                dismissAllowingStateLoss()
                chattingClickListener.invoke(userDialogInfo.id)
            }
            binding.btnOkay.addClickListener {
                Log.d("TAG", "initAfterBinding: ")
                dismissAllowingStateLoss()
                okBtnClickListerner.invoke()
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

    fun setFriendInfoDetail() {
        with(binding.contentUserInfo) {
            url = friendInfo.profileImg
            ivProfileImg.visibility = View.VISIBLE
            tvAveragePaceTitle.visibility = View.GONE
            tvCalorieTitle.visibility = View.GONE
            tvPace.text = friendInfo.pace.toRecord()
            tvDistance.text = friendInfo.totalKm.toDistance()
            tvTime.text = friendInfo.totalTime.toRecord()
        }
        binding.btnChatting.visibility = View.GONE

        binding.btnOkay.setContent("친구 요청")
        binding.btnOkay.addClickListener {
            friendViewModel.requestFriend(friendInfo.id)
            dismissAllowingStateLoss()
        }
        binding.btnDialogCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnDialogCancel
    }

}