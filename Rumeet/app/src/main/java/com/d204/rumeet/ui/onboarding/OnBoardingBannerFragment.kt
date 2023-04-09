package com.d204.rumeet.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentOnboardingBannerBinding
import com.d204.rumeet.ui.onboarding.model.OnBoardingModel

class OnBoardingBannerFragment : Fragment() {

    private lateinit var binding : FragmentOnboardingBannerBinding
    private val bannerData by lazy { arguments?.getParcelable<OnBoardingModel>("BANNER") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding_banner, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBanner.setImageResource(bannerData?.img ?: throw IllegalArgumentException("NO IMG"))
        binding.tvBannerDescription.text = bannerData?.description ?: ""
    }
}