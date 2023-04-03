package com.d204.rumeet.ui.friend.add.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemFriendListBinding
import com.d204.rumeet.databinding.ItemUserListBinding
import com.d204.rumeet.databinding.ItemUserListBindingImpl
import com.d204.rumeet.ui.friend.add.AddFriendListClickListener
import com.d204.rumeet.ui.friend.add.model.UserListUiModel

class AddFriendListAdapter(
    private val userOnClickListener: AddFriendListClickListener
) : ListAdapter<UserListUiModel, AddFriendListAdapter.AddFriendListHolder>(AddFriendListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendListHolder {
        val view = DataBindingUtil.inflate<ItemUserListBinding>(LayoutInflater.from(parent.context), R.layout.item_user_list, parent,false)
        view.clickListener = userOnClickListener
        return AddFriendListHolder(view)
    }

    override fun onBindViewHolder(holder: AddFriendListHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AddFriendListHolder(private val binding : ItemUserListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : UserListUiModel){
            binding.users = data
        }
    }

    private object AddFriendListDiffUtil : DiffUtil.ItemCallback<UserListUiModel>(){
        override fun areItemsTheSame(oldItem: UserListUiModel, newItem: UserListUiModel): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: UserListUiModel,
            newItem: UserListUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}