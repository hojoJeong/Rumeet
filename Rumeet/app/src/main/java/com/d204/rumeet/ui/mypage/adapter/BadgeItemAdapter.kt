package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemBadgeDetailBinding
import com.d204.rumeet.ui.mypage.model.BadgeDetailUiModel

class BadgeItemAdapter :
    ListAdapter<BadgeDetailUiModel, BadgeItemAdapter.BadgeItemHolder>(BadgeDiffUtil) {

    class BadgeItemHolder(private val binding: ItemBadgeDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BadgeDetailUiModel) {
            binding.badge = item
        }
    }

    object BadgeDiffUtil : DiffUtil.ItemCallback<BadgeDetailUiModel>() {
        override fun areItemsTheSame(
            oldItem: BadgeDetailUiModel,
            newItem: BadgeDetailUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BadgeDetailUiModel,
            newItem: BadgeDetailUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeItemHolder =
        BadgeItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_badge_detail, parent, false
            )
        )

    override fun onBindViewHolder(holder: BadgeItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}