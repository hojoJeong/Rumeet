package com.d204.rumeet.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentRecommendFriendBinding
import com.d204.rumeet.home.model.HomeUiModel

class ItemRecommendFriendAdapter : ListAdapter<HomeUiModel, ItemRecommendFriendAdapter.ItemRecommendFriendHolder>(RecommendFriendDiffUtil){

    class ItemRecommendFriendHolder(val binding: ItemContentRecommendFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRecommendFriendHolder = ItemRecommendFriendHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_content_recommend_friend, parent, false)
    )

    override fun onBindViewHolder(holder: ItemRecommendFriendHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object RecommendFriendDiffUtil : DiffUtil.ItemCallback<HomeUiModel>() {
        override fun areItemsTheSame(
            oldItem: HomeUiModel,
            newItem: HomeUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HomeUiModel,
            newItem: HomeUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}