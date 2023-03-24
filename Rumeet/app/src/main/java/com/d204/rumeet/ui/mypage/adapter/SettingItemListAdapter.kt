package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemSettingContentBinding

class SettingItemListAdapter :
    ListAdapter<String, SettingItemListAdapter.SettingItemHolder>(SettingItemDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemHolder =
        SettingItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_setting_content, parent, false
            )
        )

    override fun onBindViewHolder(holder: SettingItemHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class SettingItemHolder(private val binding: ItemSettingContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.title = title
            if (title == "버전 정보") {
                binding.info = "v 1.0.0"
            }
        }
    }

    object SettingItemDiffUtil : DiffUtil.ItemCallback<String>() {
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