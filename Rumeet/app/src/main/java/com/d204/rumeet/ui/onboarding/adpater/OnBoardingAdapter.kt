package com.d204.rumeet.ui.onboarding.adpater

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.d204.rumeet.ui.onboarding.OnBoardingBannerFragment
import com.d204.rumeet.ui.onboarding.model.OnBoardingModel

class OnBoardingAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    private val imgList = mutableListOf<OnBoardingModel>()

    override fun getItemCount() = imgList.size

    override fun createFragment(position: Int): Fragment {
        return OnBoardingBannerFragment().apply {
            arguments = bundleOf("BANNER" to imgList[position])
        }
    }

    fun setImg(imgList : List<OnBoardingModel>){
        this.imgList.addAll(imgList)
    }
}