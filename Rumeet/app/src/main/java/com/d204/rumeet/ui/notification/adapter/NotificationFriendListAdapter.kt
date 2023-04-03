package com.d204.rumeet.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemNotificationBinding
import com.d204.rumeet.domain.model.user.NotificationListDomainModel
import com.d204.rumeet.domain.model.user.RunningRequestDomainModel
import com.d204.rumeet.ui.notification.NotificationHandler
import com.d204.rumeet.ui.notification.model.NotificationFriendUiModel
import com.d204.rumeet.util.toDate

class NotificationFriendListAdapter : ListAdapter<NotificationListDomainModel, NotificationFriendListAdapter.NotificationFriendItemHolder>(NotificationListDiffUtil) {
    lateinit var notificationHandler: NotificationHandler
    inner class NotificationFriendItemHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationListDomainModel) {
            binding.img = item.fromUserProfile
            binding.tvItemNotificationName.text = item.fromUserName
            binding.tvItemNotificationPace.text = item.date.toDate()
            binding.btnItemNotificationAccept.setOnClickListener {
                notificationHandler.onClick(item.fromUserId, item.toUserId, true)
            }
            binding.btnItemNotificationReject.setOnClickListener {
                notificationHandler.onClick(item.fromUserId, item.toUserId, false)
            }
        }
    }

    object NotificationListDiffUtil : DiffUtil.ItemCallback<NotificationListDomainModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationListDomainModel,
            newItem: NotificationListDomainModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationListDomainModel,
            newItem: NotificationListDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationFriendItemHolder {
        return NotificationFriendItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationFriendItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}