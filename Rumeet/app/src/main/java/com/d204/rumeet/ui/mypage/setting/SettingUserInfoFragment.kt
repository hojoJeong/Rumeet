package com.d204.rumeet.ui.mypage.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentSettingUserInfoBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.MypageViewModel
import com.d204.rumeet.ui.mypage.adapter.SettingItemListAdapter

class SettingUserInfoFragment : BaseFragment<FragmentSettingUserInfoBinding, BaseViewModel>() {
    private val mypageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)

    override val layoutResourceId: Int
        get() = R.layout.fragment_setting_user_info
    override val viewModel: BaseViewModel
        get() = mypageViewModel

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView(){

        val userInfoAdapter = SettingItemListAdapter().apply{

        }
    }

}