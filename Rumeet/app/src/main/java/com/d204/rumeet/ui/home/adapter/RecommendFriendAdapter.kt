package com.d204.rumeet.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentRecommendFriendBinding
import com.d204.rumeet.ui.home.model.HomeUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

class RecommendFriendAdapter : ListAdapter<RecommendFriendUiModel, RecommendFriendAdapter.RecommendFriendHolder>(
    RecommendFriendDiffUtil
){

    class RecommendFriendHolder(val binding: ItemContentRecommendFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendFriendUiModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendFriendHolder = RecommendFriendHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_content_recommend_friend, parent, false)
    )

    override fun onBindViewHolder(holder: RecommendFriendHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object RecommendFriendDiffUtil : DiffUtil.ItemCallback<RecommendFriendUiModel>() {
        override fun areItemsTheSame(
            oldItem: RecommendFriendUiModel,
            newItem: RecommendFriendUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecommendFriendUiModel,
            newItem: RecommendFriendUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}