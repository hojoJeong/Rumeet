package com.d204.rumeet.ui.mypage.setting

import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSettingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MypageViewModel
import com.d204.rumeet.ui.mypage.adapter.SettingItemListAdapter


class SettingFragment : BaseFragment<FragmentSettingBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_setting
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        initSettingContent()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initSettingContent(){
        val settingContentAdapter = SettingItemListAdapter().apply {
            submitList( resources.getStringArray(R.array.title_setting_content).toList())
        }

        with(binding.rvSetting){
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = settingContentAdapter
        }
    }

}