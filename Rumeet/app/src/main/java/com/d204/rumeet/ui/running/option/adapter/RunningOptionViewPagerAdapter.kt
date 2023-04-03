package com.d204.rumeet.ui.running.option.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RunningOptionViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private lateinit var fragmentList: List<Fragment>


    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun setFragmentList(list: List<Fragment>) {
        fragmentList = list
    }
}