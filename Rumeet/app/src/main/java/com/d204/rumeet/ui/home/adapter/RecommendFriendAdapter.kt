package com.d204.rumeet.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentRecommendFriendBinding
import com.d204.rumeet.domain.model.friend.FriendRecommendDomainModel
import com.d204.rumeet.ui.home.HomeHandler
import com.d204.rumeet.ui.home.model.HomeUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

class RecommendFriendAdapter : ListAdapter<FriendRecommendDomainModel, RecommendFriendAdapter.RecommendFriendHolder>(
    RecommendFriendDiffUtil
){
    lateinit var homeHandler: HomeHandler
    inner class RecommendFriendHolder(private val binding: ItemContentRecommendFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendRecommendDomainModel) {
            binding.data = item
            binding.btnItemContentSeeFriendInfoDetail.setOnClickListener {
                homeHandler.onClick(item.userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendFriendHolder = RecommendFriendHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_content_recommend_friend, parent, false)
    )

    override fun onBindViewHolder(holder: RecommendFriendHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object RecommendFriendDiffUtil : DiffUtil.ItemCallback<FriendRecommendDomainModel>() {
        override fun areItemsTheSame(
            oldItem: FriendRecommendDomainModel,
            newItem: FriendRecommendDomainModel
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: FriendRecommendDomainModel,
            newItem: FriendRecommendDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}