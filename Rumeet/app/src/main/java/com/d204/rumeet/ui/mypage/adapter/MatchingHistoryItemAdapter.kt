package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemMatchingHistoryBinding
import com.d204.rumeet.databinding.ItemRunningRecordActivityBinding
import com.d204.rumeet.ui.mypage.model.BadgeDetailUiModel
import com.d204.rumeet.ui.mypage.model.MatchingHistoryRaceUiModel

class MatchingHistoryItemAdapter : ListAdapter<MatchingHistoryRaceUiModel, MatchingHistoryItemAdapter.MatchingHistoryItemHolder>(MatchingHistoryDiffUtil){

    class MatchingHistoryItemHolder(private val binding: ItemMatchingHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: MatchingHistoryRaceUiModel){
            binding.activity = item
        }
    }

    object MatchingHistoryDiffUtil : DiffUtil.ItemCallback<MatchingHistoryRaceUiModel>() {
        override fun areItemsTheSame(
            oldItem: MatchingHistoryRaceUiModel,
            newItem: MatchingHistoryRaceUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MatchingHistoryRaceUiModel,
            newItem: MatchingHistoryRaceUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingHistoryItemHolder {
        return MatchingHistoryItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_matching_history, parent, false ))
    }

    override fun onBindViewHolder(holder: MatchingHistoryItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}