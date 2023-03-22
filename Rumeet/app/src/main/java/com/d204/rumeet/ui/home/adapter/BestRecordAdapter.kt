package com.d204.rumeet.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemContentBestrecordBinding
import com.d204.rumeet.ui.home.model.BestRecordUiModel

class BestRecordAdapter :
    ListAdapter<BestRecordUiModel, BestRecordAdapter.BestRecordHolder>(BestRecordDiffUtil) {

    class BestRecordHolder(val binding: ItemContentBestrecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BestRecordUiModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRecordHolder =
        BestRecordHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_content_bestrecord,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: BestRecordHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object BestRecordDiffUtil : DiffUtil.ItemCallback<BestRecordUiModel>() {
        override fun areItemsTheSame(
            oldItem: BestRecordUiModel,
            newItem: BestRecordUiModel
        ): Boolean {
            return oldItem.value == newItem.value
        }

        override fun areContentsTheSame(
            oldItem: BestRecordUiModel,
            newItem: BestRecordUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}