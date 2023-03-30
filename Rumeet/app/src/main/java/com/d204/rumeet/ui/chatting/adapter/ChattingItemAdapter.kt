package com.d204.rumeet.ui.chatting.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemChattingLeftBinding
import com.d204.rumeet.databinding.ItemChattingRightBinding
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.ui.chatting.model.ChattingMessageUiModel

class ChattingItemAdapter(
    private val userId: Int,
    private val profile : String?,
) : ListAdapter<ChattingMessageUiModel, RecyclerView.ViewHolder>(ChattingMessageDiffUtil) {

    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
    }

    private var receiver = false

    inner class ChattingLeftMessageHolder(private val leftBinding: ItemChattingLeftBinding) :
        RecyclerView.ViewHolder(leftBinding.root) {
        fun bind(data: ChattingMessageUiModel) {
            leftBinding.profileImg = profile
            leftBinding.data = data
        }
    }

    inner class ChattingRightMessageHolder(private val rightBinding: ItemChattingRightBinding) :
        RecyclerView.ViewHolder(rightBinding.root) {
        fun bind(data: ChattingMessageUiModel) {
            rightBinding.data = data
            receiver = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (userId == item.message.fromUserId) SENDER else RECEIVER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENDER -> {
                val view = DataBindingUtil.inflate<ItemChattingRightBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_chatting_right,
                    parent,
                    false
                )
                ChattingRightMessageHolder(view)
            }
            RECEIVER -> {
                val view = DataBindingUtil.inflate<ItemChattingLeftBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_chatting_left,
                    parent,
                    false
                )
                ChattingLeftMessageHolder(view)
            }
            else -> {
                throw IllegalArgumentException("NO TYPE")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChattingRightMessageHolder -> {
                holder.bind(getItem(position))
            }
            is ChattingLeftMessageHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    private object ChattingMessageDiffUtil : DiffUtil.ItemCallback<ChattingMessageUiModel>() {
        override fun areItemsTheSame(
            oldItem: ChattingMessageUiModel,
            newItem: ChattingMessageUiModel
        ): Boolean {
            return oldItem.message.date == newItem.message.date
        }

        override fun areContentsTheSame(
            oldItem: ChattingMessageUiModel,
            newItem: ChattingMessageUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}