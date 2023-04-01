package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemRunningRecordActivityBinding
import com.d204.rumeet.domain.model.user.RunningRecordActivityDomainModel
import com.d204.rumeet.ui.mypage.model.MyPageMenuUiModel
import com.d204.rumeet.ui.mypage.model.RunningActivityUiModel

class RunningActivityListAdapter :
    ListAdapter<RunningActivityUiModel, RunningActivityListAdapter.RunningActivityItemHolder>(
        RunningActivityDiffUtil
    ) {

    class RunningActivityItemHolder(private val binding: ItemRunningRecordActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RunningActivityUiModel) {
            binding.activity = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningActivityListAdapter.RunningActivityItemHolder =
        RunningActivityItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_running_record_activity,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RunningActivityItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object RunningActivityDiffUtil : DiffUtil.ItemCallback<RunningActivityUiModel>() {
        override fun areItemsTheSame(
            oldItem: RunningActivityUiModel,
            newItem: RunningActivityUiModel
        ): Boolean {
            return oldItem.raceId == newItem.raceId
        }

        override fun areContentsTheSame(
            oldItem: RunningActivityUiModel,
            newItem: RunningActivityUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}