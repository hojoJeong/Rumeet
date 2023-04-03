package com.d204.rumeet.ui.friend.list

import android.view.View
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogFriendInfoBinding
import com.d204.rumeet.domain.model.friend.FriendInfoDomainModel
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import com.d204.rumeet.ui.base.BaseDialogFragment
import com.d204.rumeet.ui.friend.UserDialogModel
import com.d204.rumeet.ui.friend.add.AddFriendViewModel
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel
import com.d204.rumeet.ui.friend.toUserDialogModel
import com.d204.rumeet.util.toDistance
import com.d204.rumeet.util.toRecord
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendInfoDialog : BaseDialogFragment<DialogFriendInfoBinding>(R.layout.dialog_friend_info){
    override val layoutResourceId: Int
        get() = R.layout.dialog_friend_info

    private lateinit var chattingClickListener: (Int) -> Unit
    private lateinit var userDialogModel: UserDialogModel
    lateinit var viewInfo: String
    lateinit var friendInfo: FriendInfoDomainModel
    lateinit var friendViewModel: AddFriendViewModel
    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        if(viewInfo == "friend") {
            setFriendInfoDetail()
        } else {
            binding.contentUserInfo
            binding.btnChatting.setOnClickListener {
                chattingClickListener.invoke(userDialogModel.id)
                dismissAllowingStateLoss()
            }
            binding.btnOkay.setOnClickListener {
                dismissAllowingStateLoss()
            }
        }
    }

    fun addChattingButtonClickListener(event : (Int) -> Unit){
        chattingClickListener = event
    }

    fun initFriendInfo(data : FriendListUiModel){
        userDialogModel = data.toUserDialogModel()
    }

    fun setFriendInfoDetail(){
        with(binding.contentUserInfo){
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
    }

}