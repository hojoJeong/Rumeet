package com.d204.rumeet.ui.mypage.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemSettingContentBinding
import com.d204.rumeet.ui.mypage.MypageViewModel
import com.d204.rumeet.ui.mypage.model.SettingOptionUiMdel

class SettingItemListAdapter :
    ListAdapter<SettingOptionUiMdel, SettingItemListAdapter.SettingItemHolder>(SettingItemDiffUtil) {
    lateinit var viewModel: MypageViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemHolder =
        SettingItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_setting_content, parent, false
            )
        )

    override fun onBindViewHolder(holder: SettingItemHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class SettingItemHolder(private val binding: ItemSettingContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: SettingOptionUiMdel) {
            with(binding) {
                title = option.title
                info = option.content
                vm = viewModel
            }
        }
    }

    object SettingItemDiffUtil : DiffUtil.ItemCallback<SettingOptionUiMdel>() {
        override fun areItemsTheSame(
            oldItem: SettingOptionUiMdel,
            newItem: SettingOptionUiMdel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SettingOptionUiMdel,
            newItem: SettingOptionUiMdel
        ): Boolean {
            return oldItem == newItem
        }
    }
}