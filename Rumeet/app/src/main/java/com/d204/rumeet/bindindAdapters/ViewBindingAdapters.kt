package com.d204.rumeet.bindindAdapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel
import com.d204.rumeet.ui.chatting.adapter.ChattingItemAdapter
import com.d204.rumeet.ui.chatting.chatting_list.adapter.ChattingListAdapter
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
import com.d204.rumeet.ui.chatting.model.ChattingMessageUiModel
import com.d204.rumeet.ui.components.FilledEditText
import com.d204.rumeet.ui.friend.add.adapter.AddFriendListAdapter
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.list.adapter.FriendListAdapter
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel
import com.d204.rumeet.ui.notification.adapter.NotificationFriendListAdapter
import com.d204.rumeet.util.scrollToBottom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@BindingAdapter("isEnable")
fun FilledEditText.bindIsEnable(state: Boolean) {
    binding.editInput.isEnabled = state
}

@BindingAdapter("friends")
fun RecyclerView.bindFriendsList(uiState: UiState<List<FriendListDomainModel>>) {
    val bindAdapter = this.adapter
    if (bindAdapter is FriendListAdapter) {
        bindAdapter.submitList(uiState.successOrNull())
    }
}

@BindingAdapter("users")
fun RecyclerView.bindUserList(uiState: UiState<List<UserListUiModel>>) {
    val bindAdapter = this.adapter
    if (bindAdapter is AddFriendListAdapter) {
        bindAdapter.submitList(uiState.successOrNull())
    }
}

@BindingAdapter("friend_list")
fun RecyclerView.bindNotiList(uiState: UiState<List<NotificationListDomainModel>>){
    val bindAdapter = this.adapter
    if(bindAdapter is NotificationFriendListAdapter){
        bindAdapter.submitList(uiState.successOrNull()?: emptyList<NotificationListDomainModel>())
    }
}

@BindingAdapter("chatting_rooms")
fun RecyclerView.bindChattingRoom(uiState: UiState<List<ChattingRoomUiModel>>) {
    val bindAdapter = this.adapter
    if (bindAdapter is ChattingListAdapter) {
        bindAdapter.submitList(uiState.successOrNull())
    }
}

@BindingAdapter("chatting")
fun RecyclerView.bindChattingData(uiState: UiState<List<ChattingMessageUiModel>>) {
    val bindAdapter = this.adapter
    if (bindAdapter is ChattingItemAdapter) {
        val list = uiState.successOrNull()
        bindAdapter.submitList(list)
    }
    scrollToBottom()
}