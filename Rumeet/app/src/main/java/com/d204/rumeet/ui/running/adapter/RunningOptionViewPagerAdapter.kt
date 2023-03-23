package com.d204.rumeet.ui.running.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.d204.rumeet.databinding.FragmentRunningOptionCompetitionOrGhostBinding
import com.d204.rumeet.ui.running.fragment.RunningOptionCompetitionOrGhostFragment

class RunningOptionViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var tabCount = 0
    private lateinit var fragmentList: List<Fragment>

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun setTabCount(count: Int) {
        tabCount = count
    }

    fun setFragmentList(list: List<Fragment>) {
        fragmentList = list
    }
}