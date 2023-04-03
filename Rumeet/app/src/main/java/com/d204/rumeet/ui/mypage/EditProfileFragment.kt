package com.d204.rumeet.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentEditProfileBinding
import com.d204.rumeet.ui.base.BaseFragment

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_edit_profile
    override val viewModel: MyPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage){defaultViewModelProviderFactory}

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initView(){

    }

}