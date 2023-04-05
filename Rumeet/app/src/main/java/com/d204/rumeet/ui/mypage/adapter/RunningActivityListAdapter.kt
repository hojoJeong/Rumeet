package com.d204.rumeet.ui.mypage.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemRunningRecordActivityBinding
import com.d204.rumeet.domain.model.user.RunningRecordActivityDomainModel
import com.d204.rumeet.ui.mypage.MapDialog
import com.d204.rumeet.ui.mypage.model.MyPageMenuUiModel
import com.d204.rumeet.ui.mypage.model.RunningActivityUiModel

class RunningActivityListAdapter(private val fragmentManager: FragmentManager) :
    ListAdapter<RunningActivityUiModel, RunningActivityListAdapter.RunningActivityItemHolder>(
        RunningActivityDiffUtil
    ) {

    class RunningActivityItemHolder(private val binding: ItemRunningRecordActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RunningActivityUiModel, index: Int, myFragmentManager: FragmentManager) {
            Log.d(TAG, "bind: $item")
            binding.activity = item
            binding.index = index.toString()

            binding.root.setOnClickListener {
                // 지도 상세보기 다이얼로그 띄우기
                val dialog = MapDialog(binding.root.context, "러닝경로 상세보기", item.polyLine)
                dialog.show(myFragmentManager, "map")
            }
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
        holder.bind(getItem(position), itemCount, fragmentManager)
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