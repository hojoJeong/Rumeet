package com.d204.rumeet.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ItemMypageMenuBinding
import com.d204.rumeet.ui.mypage.MyPageEventHandler
import com.d204.rumeet.ui.mypage.MyPageViewModel
import com.d204.rumeet.ui.mypage.model.MyPageMenuUiModel
import kotlinx.coroutines.NonDisposableHandle.parent

class MyPageMenuAdapter(
    private val handler: MyPageEventHandler
) : ListAdapter<MyPageMenuUiModel, MyPageMenuAdapter.MyPageMenuItemHolder>(MyPageDiffUtil) {

    inner class MyPageMenuItemHolder(private val binding: ItemMypageMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyPageMenuUiModel) {
            binding.title = item.title
            binding.img = item.img
        }
    }

    object MyPageDiffUtil : DiffUtil.ItemCallback<MyPageMenuUiModel>() {
        override fun areItemsTheSame(
            oldItem: MyPageMenuUiModel,
            newItem: MyPageMenuUiModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: MyPageMenuUiModel,
            newItem: MyPageMenuUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageMenuItemHolder {
        val view =
            DataBindingUtil.inflate<ItemMypageMenuBinding>(
                LayoutInflater.from(parent.context), R.layout.item_mypage_menu, parent, false
            )
        view.handler = handler
        return MyPageMenuItemHolder(view)
    }

    override fun onBindViewHolder(holder: MyPageMenuItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}