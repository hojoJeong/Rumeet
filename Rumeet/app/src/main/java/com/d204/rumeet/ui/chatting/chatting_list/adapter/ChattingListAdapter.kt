package com.d204.rumeet.ui.chatting.chatting_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemChattingListBinding
import com.d204.rumeet.ui.chatting.chatting_list.ChattingRoomClickListener
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel

class ChattingListAdapter(
    private val chattingRoomClickListener: ChattingRoomClickListener
) : ListAdapter<ChattingRoomUiModel, ChattingListAdapter.ChattingRoomHolder>(ChattingRoomDiffUtil){

    class ChattingRoomHolder(private val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(chattingRoomData : ChattingRoomUiModel){
            binding.chattingRoomData = chattingRoomData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingRoomHolder {
        val view = DataBindingUtil.inflate<ItemChattingListBinding>(LayoutInflater.from(parent.context), R.layout.item_chatting_list, parent, false)
        view.clickListener = chattingRoomClickListener
        return ChattingRoomHolder(view)
    }

    override fun onBindViewHolder(holder: ChattingRoomHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object ChattingRoomDiffUtil : DiffUtil.ItemCallback<ChattingRoomUiModel>(){
        override fun areItemsTheSame(
            oldItem: ChattingRoomUiModel,
            newItem: ChattingRoomUiModel
        ): Boolean {
            return oldItem.roomId == newItem.roomId
        }

        override fun areContentsTheSame(
            oldItem: ChattingRoomUiModel,
            newItem: ChattingRoomUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}