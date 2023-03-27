package com.d204.rumeet.bindindAdapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.FilledEditText
import com.d204.rumeet.ui.friend_list.adapter.FriendListAdapter
import com.d204.rumeet.ui.friend_list.model.FriendInfoModel

@BindingAdapter("isEnable")
fun FilledEditText.bindIsEnable(state : Boolean){
    binding.editInput.isEnabled = state
}

@BindingAdapter("friends")
fun RecyclerView.bindFriendsList(uiState : UiState<List<FriendInfoModel>>){
    val bindAdapter = this.adapter
    if(bindAdapter is FriendListAdapter){
        bindAdapter.submitList(uiState.successOrNull())
    }
}