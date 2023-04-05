package com.d204.rumeet.ui.running.option.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentRecommendFriendBinding
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.domain.model.friend.FriendRecommendDomainModel
import com.d204.rumeet.ui.home.HomeHandler
import com.d204.rumeet.ui.home.adapter.RecommendFriendAdapter
import com.d204.rumeet.ui.home.model.HomeUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

class SelectFriendListAdapter : ListAdapter<FriendListDomainModel, SelectFriendListAdapter.SelectFriendHolder>(
    SelectFriendDiffUtil
){
    lateinit var handler: HomeHandler
    inner class SelectFriendHolder(private val binding: ItemContentRecommendFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendListDomainModel) {
            binding.selectUrl = item.profileImage
            binding.selectName = item.nickname
            binding.btnItemContentSeeFriendInfoDetail.text = "선택"
            binding.btnItemContentSeeFriendInfoDetail.setOnClickListener {
                handler.onClick(item.userId)
            }
        }
    }


    object SelectFriendDiffUtil : DiffUtil.ItemCallback<FriendListDomainModel>() {
        override fun areItemsTheSame(
            oldItem: FriendListDomainModel,
            newItem: FriendListDomainModel
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: FriendListDomainModel,
            newItem: FriendListDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectFriendHolder {
        return SelectFriendHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_content_recommend_friend, parent, false))
    }

    override fun onBindViewHolder(holder: SelectFriendHolder, position: Int) {
        holder.bind(getItem(position))
    }
}