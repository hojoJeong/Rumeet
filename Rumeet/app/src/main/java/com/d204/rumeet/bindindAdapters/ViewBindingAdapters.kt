package com.d204.rumeet.bindindAdapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.FilledEditText
import com.d204.rumeet.ui.friend.add.adapter.AddFriendListAdapter
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.list.adapter.FriendListAdapter
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel

@BindingAdapter("isEnable")
fun FilledEditText.bindIsEnable(state: Boolean) {
    binding.editInput.isEnabled = state
}

@BindingAdapter("friends")
fun RecyclerView.bindFriendsList(uiState: UiState<List<FriendListUiModel>>) {
    val bindAdapter = this.adapter
    if (bindAdapter is FriendListAdapter) {
        bindAdapter.submitList(uiState.successOrNull())
    }
}

@BindingAdapter("users")
fun RecyclerView.bindUserList(uiState: UiState<List<UserListUiModel>>){
    val bindAdapter = this.adapter
    if(bindAdapter is AddFriendListAdapter){
        bindAdapter.submitList(uiState.successOrNull())
    }
}