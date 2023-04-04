package com.d204.rumeet.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemNotificationBinding
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel
import com.d204.rumeet.ui.mypage.model.BadgeContentListUiModel
import com.d204.rumeet.ui.notification.NotificationHandler
import com.d204.rumeet.ui.notification.model.NotificationRunningUiModel
import com.d204.rumeet.util.toDate
import com.d204.rumeet.util.toMode

class NotificationRunningListAdapter: ListAdapter<RunningRequestDomainModel, NotificationRunningListAdapter.NotificationRunningItemHolder>(NotificationListDiffUtil) {
    lateinit var handler: NotificationHandler
    inner class NotificationRunningItemHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RunningRequestDomainModel) {
            binding.tvItemNotificationPace.text = item.date.toDate()
            val mode = item.mode.toMode()
            binding.tvItemNotificationMode.text = mode.substring(0, 2)
            binding.tvItemNotificationOption.text = mode.substring(3)
            binding.btnItemNotificationAccept.setOnClickListener {
                handler.onClickRunning(item.raceId, true)
            }
            binding.btnItemNotificationReject.setOnClickListener {
                handler.onClickRunning(item.raceId, false)
            }
        }
    }

    object NotificationListDiffUtil : DiffUtil.ItemCallback<RunningRequestDomainModel>() {
        override fun areItemsTheSame(
            oldItem: RunningRequestDomainModel,
            newItem: RunningRequestDomainModel
        ): Boolean {
            return oldItem.raceId == newItem.raceId
        }

        override fun areContentsTheSame(
            oldItem: RunningRequestDomainModel,
            newItem: RunningRequestDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRunningItemHolder {
        return NotificationRunningItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationRunningItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}