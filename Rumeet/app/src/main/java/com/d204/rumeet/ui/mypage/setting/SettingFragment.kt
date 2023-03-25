package com.d204.rumeet.ui.mypage.setting

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSettingBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MyPageAction
import com.d204.rumeet.ui.mypage.MypageViewModel
import com.d204.rumeet.ui.mypage.adapter.SettingItemListAdapter
import com.d204.rumeet.ui.mypage.model.SettingOptionUiMdel
import kotlinx.coroutines.flow.collectLatest


class SettingFragment : BaseFragment<FragmentSettingBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_setting
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {

        initView()
    }

    override fun initDataBinding() {
        myPageViewModel.setOptionList(resources.getStringArray(R.array.title_setting_content).toList())
        lifecycleScope.launchWhenResumed {
            myPageViewModel.navigationEvent.collectLatest { action ->
                if(action == MyPageAction.UserInfo){
                    Log.d(TAG, "initDataBinding: 세팅 네비게이트")
                    navigate(SettingFragmentDirections.actionSettingFragmentToSettingUserInfoFragment())
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initView(){
        val titleList = resources.getStringArray(R.array.title_setting_content).toList()
        val settingOptionList = titleList.mapIndexed { index, title ->
            SettingOptionUiMdel(title, "")
        }
        settingOptionList[titleList.indexOf("버전 정보")].content = "v 0.0.1"

        val settingContentAdapter = SettingItemListAdapter().apply {
            submitList(settingOptionList)
        }

        with(binding.rvSetting){
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = settingContentAdapter
        }
    }

}