package com.d204.rumeet.ui.mypage.setting

import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSettingUserInfoBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.MyPageViewModel
import com.d204.rumeet.ui.mypage.adapter.SettingItemListAdapter
import com.d204.rumeet.ui.mypage.model.SettingOptionUiMdel
import com.d204.rumeet.util.checkEmailValidate
import com.d204.rumeet.util.hashingSHA256
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoFragment : BaseFragment<FragmentSettingUserInfoBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_setting_user_info
    override val viewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage) { defaultViewModelProviderFactory }

    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
        initUserInfoAction()
    }

    override fun initAfterBinding() {
    }

    private fun initView() {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.userInfo.collect{
                    val userInfo = viewModel.userInfo.value.successOrNull()!!
                    val userInfoOptionTitleList =
                        resources.getStringArray(R.array.title_user_info_content).toList()
                    viewModel.setUserInfoMenuTitleList(userInfoOptionTitleList)

                    val settingOptionList = userInfoOptionTitleList.mapIndexed { _, title ->
                        SettingOptionUiMdel(title, "")
                    }.apply {
                        this[0].content = if(checkEmailValidate(userInfo.email) || userInfo.oauth == null) userInfo.email else "소셜로그인입니다."
                        this[1].content = userInfo.nickname
                        this[2].content = "${userInfo.age}세"
                        this[3].content = if(userInfo.gender == 0) "남자" else "여자"
                        this[4].content = "${userInfo.height}cm / ${userInfo.weight}kg"
                    }
                    initUserInfoAdapter(settingOptionList)
                }
            }
        }
    }

    private fun initUserInfoAdapter(settingOptionList: List<SettingOptionUiMdel>){
        val userInfoAdapter = SettingItemListAdapter(viewModel).apply {
            submitList(settingOptionList)
        }

        with(binding.rvSettingUserInfo) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userInfoAdapter
        }
    }

    private fun initUserInfoAction() {
        lifecycleScope.launchWhenResumed {
            viewModel.userInfoNavigationEvent.collectLatest { action ->
                when (action) {
                    UserInfoAction.ResetDetailInfo -> {
                        navigate(
                            UserInfoFragmentDirections.actionUserInfoFragmentToAdditionalInfoFragment2(
                                reset = true
                            )
                        )
                    }
                    UserInfoAction.ResetPassword -> {
                        if(viewModel.userInfo.value.successOrNull()?.oauth.isNullOrEmpty()){
                            navigate(
                                UserInfoFragmentDirections.actionUserInfoFragmentToResetPasswordFragment2("")
                            )
                        } else {
                            toastMessage("소셜로그인은 비밀번호를 변경할 수 없습니다.")
                        }

                    }
                    UserInfoAction.Withdrawal -> {
                        navigate(UserInfoFragmentDirections.actionUserInfoFragmentToWithdrawalFragment())
                    }
                }
            }
        }
    }

}