package com.d204.rumeet.ui.onboarding

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentOnboardingBinding
import com.d204.rumeet.ui.activities.LoginActivity
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.onboarding.adpater.OnBoardingAdapter
import com.d204.rumeet.ui.onboarding.model.OnBoardingModel
import com.d204.rumeet.util.startActivityAfterClearBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding, OnBoardingViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_onboarding
    override val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var onBoardingAdapter : OnBoardingAdapter

    // 뷰모델 실행
    override fun initStartView() {
        binding.apply {
            this.vm = viewModel
            this.lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    // 뷰모델 초기 설정
    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.startToLogin.collectLatest { state ->
                if (state) {
                    requireContext().startActivityAfterClearBackStack(LoginActivity::class.java)
                }
            }
        }
    }

    // view 설정
    override fun initAfterBinding() {
        binding.btnOnboardingContinue.also {
            it.setContent("계속하기")
            it.setState(true)
        }
        onBoardingAdapter = OnBoardingAdapter(this).apply {
            setImg(
                listOf(
                    OnBoardingModel(R.drawable.bg_onboarding_1, "러닝할 준비 되셨나요?\n나만의 러닝메이트를 찾아보아요!"),
                    OnBoardingModel(R.drawable.bg_onboarding_2, "나의 체력데이터로\n러닝메이트를 찾아줘요"),
                    OnBoardingModel(R.drawable.bg_onboarding_3, "혼자서 달리기는 그만!\n경쟁과 협동을 동시에 즐겨요!"),
                )
            )
        }
        binding.vpOnboardingBanner.adapter = onBoardingAdapter
        binding.ciOnboardingIndicator.setViewPager(binding.vpOnboardingBanner)
    }
}