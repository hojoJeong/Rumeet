package com.d204.rumeet.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentBadgeBinding

class BadgeAdapter : ListAdapter<String, BadgeAdapter.BadgeHolder>(BadgeDiffUtil) {

    class BadgeHolder(val binding: ItemContentBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            binding.url = imgUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeHolder =
        BadgeHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_content_badge,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BadgeHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object BadgeDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }
}