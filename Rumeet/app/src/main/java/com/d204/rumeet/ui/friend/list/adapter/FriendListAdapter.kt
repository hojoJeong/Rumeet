package com.d204.rumeet.ui.friend.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemFriendListBinding
import com.d204.rumeet.domain.model.friend.FriendListDomainModel
import com.d204.rumeet.ui.friend.list.FriendListClickListener
import com.d204.rumeet.ui.friend.list.model.FriendListUiModel

class FriendListAdapter(
    private val friendListClickListener: FriendListClickListener
) : ListAdapter<FriendListDomainModel, FriendListAdapter.FriendListViewHolder>(FriendListDiffUtil) {

    class FriendListViewHolder(private val binding : ItemFriendListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : FriendListDomainModel){
            binding.friend = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val view = DataBindingUtil.inflate<ItemFriendListBinding>(LayoutInflater.from(parent.context), R.layout.item_friend_list, parent, false)
        view.clickListener = friendListClickListener
        return FriendListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object FriendListDiffUtil : DiffUtil.ItemCallback<FriendListDomainModel>(){
        override fun areItemsTheSame(oldItem: FriendListDomainModel, newItem: FriendListDomainModel): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: FriendListDomainModel,
            newItem: FriendListDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}