package com.d204.rumeet.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentBestrecordBinding
import com.d204.rumeet.home.model.HomeUiModel

class ItemBestRecordAdapter :
    ListAdapter<HomeUiModel, ItemBestRecordAdapter.ItemBestRecordHolder>(HomeDiffUtil) {

    class ItemBestRecordHolder(val binding: ItemContentBestrecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBestRecordHolder =
        ItemBestRecordHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_content_bestrecord,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ItemBestRecordHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object HomeDiffUtil : DiffUtil.ItemCallback<HomeUiModel>() {
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