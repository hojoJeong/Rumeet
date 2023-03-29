package com.d204.rumeet.bindindAdapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.chatting.adapter.ChattingItemAdapter
import com.d204.rumeet.ui.chatting.chatting_list.adapter.ChattingListAdapter
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
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

@BindingAdapter("chatting_rooms")
fun RecyclerView.bindChattingRoom(uiState: UiState<List<ChattingRoomUiModel>>){
    val bindAdapter = this.adapter
    if(bindAdapter is ChattingListAdapter){
        bindAdapter.submitList(uiState.successOrNull())
    }
}

@BindingAdapter("chatting")
fun RecyclerView.bindChattingData(uiState: UiState<List<ChattingMessageModel>>){
    val bindAdapter = this.adapter
    if(bindAdapter is ChattingItemAdapter){
        bindAdapter.submitList(uiState.successOrNull())
    }
}