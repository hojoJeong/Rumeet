package com.d204.rumeet.bindindAdapters

import androidx.databinding.BindingAdapter
import com.d204.rumeet.R
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.CardViewHome
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.ui.chatting.adapter.ChattingItemAdapter
import com.d204.rumeet.ui.chatting.chatting_list.adapter.ChattingListAdapter
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
import com.d204.rumeet.ui.components.FilledEditText
import com.d204.rumeet.ui.friend.add.adapter.AddFriendListAdapter
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import com.d204.rumeet.ui.friend.list.adapter.FriendListAdapter
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

@BindingAdapter("isEnable")
fun FilledEditText.bindIsEnable(state: Boolean) {
    binding.editInput.isEnabled = state
}

@BindingAdapter("setBestRecordView")
fun CardViewHome.setBestRecordView(uiState: UiState<List<BestRecordUiModel>>){
    val title = context.getString(R.string.title_best_record)
    if(uiState.successOrNull()!!.isEmpty()){
        setViewWhenEmptyData(title, context.getString(R.string.content_no_best_record))
    } else {
        setViewContent(title, null)
    }
}

@BindingAdapter("setBadgeView")
fun CardViewHome.setBadgeView(uiState: UiState<List<String>>){
    val title = context.getString(R.string.title_my_badge)
    if(uiState.successOrNull()!!.isEmpty()){
        setViewWhenEmptyData(title, context.getString(R.string.content_no_badge))
    } else {
        setViewContent(title, context.getDrawable(R.drawable.ic_arrow_right)!!)
    }
}

@BindingAdapter("setRecommendFriendView")
fun CardViewHome.setRecommendFriendView(uiState: UiState<List<RecommendFriendUiModel>>){
    val title = context.getString(R.string.title_recommend_random_friend)
    if(uiState.successOrNull()!!.isEmpty()){
        setViewWhenEmptyData(title, context.getString(R.string.content_no_pace))
    } else {
        setViewContent(title, context.getDrawable(R.drawable.ic_refresh)!!)
    }
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
        val list = uiState.successOrNull()
        bindAdapter.submitList(list)
        smoothScrollToPosition(list?.size ?: 0)
    }
}