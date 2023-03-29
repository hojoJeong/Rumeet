package com.d204.rumeet.ui.mypage.setting

import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSettingBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.mypage.MyPageViewModel
import com.d204.rumeet.ui.mypage.adapter.SettingItemListAdapter
import com.d204.rumeet.ui.mypage.model.SettingOptionUiMdel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, BaseViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_setting
    override val viewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage){defaultViewModelProviderFactory}


    override fun initStartView() {
        initView()
    }

    override fun initDataBinding() {
        viewModel.setSettingMenuTitleList(
            resources.getStringArray(R.array.title_setting_content).toList()
        )
        lifecycleScope.launchWhenResumed {
            viewModel.settingNavigationEvent.collectLatest { action ->
                when (action) {
                    SettingAction.UserInfo -> {
                        navigate(SettingFragmentDirections.actionSettingFragmentToUserInfoFragment())
                    }
                    SettingAction.SettingNotification -> {
                        navigate(SettingFragmentDirections.actionSettingFragmentToNotificationSettingFragment())
                    }
                    SettingAction.Privacy -> {
                    }
                    SettingAction.ServiceTerms -> {
                    }
                    SettingAction.LogOut -> {
                        showLogoutDialog()
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initView() {
        val titleList = resources.getStringArray(R.array.title_setting_content).toList()
        val settingOptionList = titleList.mapIndexed { _, title ->
            SettingOptionUiMdel(title, "")
        }
        settingOptionList[titleList.indexOf(resources.getStringArray(R.array.title_setting_content)[2])].content =
            "v 0.0.1"

        val settingContentAdapter = SettingItemListAdapter(viewModel).apply {
            submitList(settingOptionList)
        }

        with(binding.rvSetting) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = settingContentAdapter
        }
    }

    private fun showLogoutDialog() {
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(
                title = "알림 메시지",
                content = "로그아웃 하시겠습니까?",
                buttonText = "로그아웃"
            )
        ).apply {
            setCancelButtonVisibility(true)
        }
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }
}