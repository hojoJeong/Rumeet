package com.d204.rumeet.ui.mypage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMyPageBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.mypage.adapter.MyPageMenuAdapter
import com.d204.rumeet.ui.mypage.model.MyPageMenuUiModel
import kotlinx.coroutines.flow.collectLatest

class MyPageFragment : BaseFragment<FragmentMyPageBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MypageViewModel>(R.id.navigation_mypage)
    override val layoutResourceId: Int
        get() = R.layout.fragment_my_page
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        initText()
        initMenu()
    }

    override fun initDataBinding() {
        binding.vm = myPageViewModel
        lifecycleScope.launchWhenResumed {
            myPageViewModel.myPageNavigationEvent.collectLatest { action ->
                when (action) {
                    MyPageAction.BadgeList -> navigate(MyPageFragmentDirections.actionMyPageFragmentToBadgeListFragment())
                    MyPageAction.EditProfile -> navigate(MyPageFragmentDirections.actionMyPageFragmentToJoinNicknameFragment2(1,"", true))
                    MyPageAction.FriendList -> navigate(MyPageFragmentDirections.actionMyPageFragmentToFriendListFragment())
                    MyPageAction.LogOut -> showLogoutDialog()
                    MyPageAction.MatchingHistory -> navigate(MyPageFragmentDirections.actionMyPageFragmentToMatchingHistoryFragment())
                    MyPageAction.RunningRecord -> navigate(MyPageFragmentDirections.actionMyPageFragmentToRunningRecordFragment())
                    MyPageAction.Setting -> navigate(MyPageFragmentDirections.actionMyPageFragmentToSettingFragment())
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initText() {
        //TODO(사용자 닉네임 임시)
        val userName = "배달전문 박정은"
        val message = getString(R.string.content_mypage_message_top)
        val messageBuilder = SpannableStringBuilder(message).apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.navy_blue)),
                4, message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvMypageMessage.text = messageBuilder

        val content = getString(R.string.content_mypage_message_include_name, userName)
        val builder = SpannableStringBuilder(content).apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.navy_blue)),
                0, userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvMypageName.text = builder

    }

    private fun initMenu() {
        val menuTitleList = resources.getStringArray(R.array.title_mypage_menu).toList()
        myPageViewModel.setMyPageMunuTitleList(menuTitleList)

        val menuImgList = listOf(
            R.drawable.ic_running_record,
            R.drawable.ic_matching_history,
            R.drawable.ic_friend,
            R.drawable.ic_badge,
            R.drawable.ic_edit_profile,
            R.drawable.ic_setting,
            R.drawable.ic_logout
        )

        val menuList = menuTitleList.mapIndexed { index, title ->
            MyPageMenuUiModel(title, menuImgList[index])
        }

        val myPageMenuAdapter = MyPageMenuAdapter().apply {
            submitList(menuList)
            viewModel = myPageViewModel
        }
        with(binding.rvMypageMenu) {
            layoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            adapter = myPageMenuAdapter
            bringToFront()
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