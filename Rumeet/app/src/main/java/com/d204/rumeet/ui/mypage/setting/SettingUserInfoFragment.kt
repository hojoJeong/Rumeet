package com.d204.rumeet.ui.mypage.setting

import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSettingUserInfoBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MypageViewModel
import com.d204.rumeet.ui.mypage.adapter.SettingItemListAdapter
import com.d204.rumeet.ui.mypage.model.SettingOptionUiMdel
import kotlinx.coroutines.flow.collectLatest

class SettingUserInfoFragment : BaseFragment<FragmentSettingUserInfoBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_setting_user_info
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
        initUserInfoAction()
    }

    override fun initAfterBinding() {
    }

    private fun initView() {

        //TODO(임시 데이터)
        val userInfo = listOf<String>(
            "ssafy@naver.com",
            "김싸피피피피피피",
            "23",
            "남",
            "183cm/15kg"
        )

        val userInfoOptionTitleList = resources.getStringArray(R.array.title_user_info_content).toList()
        myPageViewModel.setUserInfoMenuTitleList(userInfoOptionTitleList)

        val settingOptionList = userInfoOptionTitleList.mapIndexed { _, title ->
            SettingOptionUiMdel(title, "")
        }.apply {
            for (idx in userInfo.indices) {
                this[idx].content = userInfo[idx]
            }
        }

        val userInfoAdapter = SettingItemListAdapter().apply {
            submitList(settingOptionList)
            viewModel = myPageViewModel
        }

        with(binding.rvSettingUserInfo) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userInfoAdapter
        }
    }

    private fun initUserInfoAction() {
        lifecycleScope.launchWhenResumed {
            myPageViewModel.userInfoNavigationEvent.collectLatest { action ->
                when(action){
                    UserInfoAction.ResetDetailInfo ->{
                        navigate(SettingUserInfoFragmentDirections.actionSettingUserInfoFragmentToConfirmPasswordFragment("resetDetailUserInfo"))
                    }
                    UserInfoAction.ResetPassword -> {
                        navigate(SettingUserInfoFragmentDirections.actionSettingUserInfoFragmentToConfirmPasswordFragment("resetPassword"))
                    }
                    UserInfoAction.Withdrawal -> {
                        navigate(SettingUserInfoFragmentDirections.actionSettingUserInfoFragmentToConfirmPasswordFragment("withdrawal"))
                    }
                }
            }
        }
    }

}