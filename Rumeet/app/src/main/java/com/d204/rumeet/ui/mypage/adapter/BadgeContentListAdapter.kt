package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemBadgeContentBinding
import com.d204.rumeet.ui.mypage.model.BadgeContentListUiModel

class BadgeContentListAdapter :
    ListAdapter<BadgeContentListUiModel, BadgeContentListAdapter.BadgeContentHolder>(BadgeContentDiffUtil) {

    class BadgeContentHolder(private val binding: ItemBadgeContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BadgeContentListUiModel) {
            binding.title = item.title

            val badgeItemListAdapter = BadgeItemAdapter().apply {
                submitList(item.badgeList)
            }
            with(binding.rvItemBadgeList){
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = badgeItemListAdapter
            }
        }
    }

    object BadgeContentDiffUtil : DiffUtil.ItemCallback<BadgeContentListUiModel>() {
        override fun areItemsTheSame(
            oldItem: BadgeContentListUiModel,
            newItem: BadgeContentListUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BadgeContentListUiModel,
            newItem: BadgeContentListUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeContentHolder =
        BadgeContentHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_badge_content, parent, false
            )
        )

    override fun onBindViewHolder(holder: BadgeContentHolder, position: Int) {
        holder.bind(getItem(position))
    }
}