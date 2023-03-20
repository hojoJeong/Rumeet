package com.d204.rumeet.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentBestrecordBinding

class ItemBadgeAdapter : ListAdapter<HomeUiModel, ItemBadgeAdapter.ItemBadgeHolder>(BadgeDiffUtil) {

    class ItemBadgeHolder(val binding: ItemContentBestrecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBadgeHolder =
        ItemBadgeHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_content_badge,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemBadgeHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object BadgeDiffUtil : DiffUtil.ItemCallback<HomeUiModel>() {
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