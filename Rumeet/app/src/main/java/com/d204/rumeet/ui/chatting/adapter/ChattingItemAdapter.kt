package com.d204.rumeet.ui.chatting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemChattingDateBinding
import com.d204.rumeet.databinding.ItemChattingLeftBinding
import com.d204.rumeet.databinding.ItemChattingRightBinding
import com.d204.rumeet.domain.model.chatting.ChattingMessageModel
import com.d204.rumeet.domain.model.chatting.ChattingModel
import java.util.concurrent.atomic.AtomicBoolean

class ChattingItemAdapter(
    private val userId: Int,
    private val profile : String?,
) : ListAdapter<ChattingMessageModel, RecyclerView.ViewHolder>(ChattingMessageDiffUtil) {

    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
    }

    private var receiver = false

    inner class ChattingLeftMessageHolder(private val leftBinding: ItemChattingLeftBinding) :
        RecyclerView.ViewHolder(leftBinding.root) {
        fun bind(data: ChattingMessageModel) {
            leftBinding.profileImg = profile
            leftBinding.data = data
            // 프로필 이미지를 보여주는 조건
            if(receiver || adapterPosition == 0) {
                leftBinding.ivItemChattingProfileOther.visibility = View.VISIBLE
                receiver = false
            }
        }
    }

    inner class ChattingRightMessageHolder(private val rightBinding: ItemChattingRightBinding) :
        RecyclerView.ViewHolder(rightBinding.root) {
        fun bind(data: ChattingMessageModel) {
            rightBinding.data = data
            receiver = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (userId == item.toUserId) SENDER else RECEIVER
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

    private object ChattingMessageDiffUtil : DiffUtil.ItemCallback<ChattingMessageModel>() {
        override fun areItemsTheSame(
            oldItem: ChattingMessageModel,
            newItem: ChattingMessageModel
        ): Boolean {
            return oldItem.createdAt == newItem.createdAt
        }

        override fun areContentsTheSame(
            oldItem: ChattingMessageModel,
            newItem: ChattingMessageModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}