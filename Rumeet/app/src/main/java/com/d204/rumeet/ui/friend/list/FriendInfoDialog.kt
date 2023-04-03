package com.d204.rumeet.ui.friend.list

import android.view.View
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogFriendInfoBinding
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
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
    lateinit var viewInfo: String
    lateinit var friendInfo: UserInfoDomainModel
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
            Glide.with(requireContext()).load(friendInfo.profile).into(ivProfileImg)
            tvAveragePaceTitle.visibility = View.GONE
            tvCalorieTitle.visibility = View.GONE

        }
    }

}