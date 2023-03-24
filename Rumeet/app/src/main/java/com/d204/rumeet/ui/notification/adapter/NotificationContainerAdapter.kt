package com.d204.rumeet.ui.notification.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.d204.rumeet.ui.notification.model.NotificationFriendUiModel
import com.d204.rumeet.ui.notification.model.NotificationRunningUiModel

class NotificationContainerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private lateinit var fragmentList: List<Fragment>

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun setFragmentList(list: List<Fragment>){
        fragmentList = list
    }
}