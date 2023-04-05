package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemMatchingHistoryBinding
import com.d204.rumeet.databinding.ItemRunningRecordActivityBinding
import com.d204.rumeet.ui.mypage.MapDialog
import com.d204.rumeet.ui.mypage.model.BadgeDetailUiModel
import com.d204.rumeet.ui.mypage.model.MatchingHistoryRaceUiModel

class MatchingHistoryItemAdapter(private val fragmentManager: FragmentManager) : ListAdapter<MatchingHistoryRaceUiModel, MatchingHistoryItemAdapter.MatchingHistoryItemHolder>(MatchingHistoryDiffUtil){
    class MatchingHistoryItemHolder(private val binding: ItemMatchingHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: MatchingHistoryRaceUiModel, myFragmentManager:FragmentManager){
            binding.activity = item
            binding.root.setOnClickListener {
                // 지도 상세보기 다이얼로그 띄우기
                val dialog = MapDialog(binding.root.context, "러닝경로 상세보기", item.polyline)
                dialog.show(myFragmentManager, "map")
            }
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
        holder.bind(getItem(position), fragmentManager)
    }
}