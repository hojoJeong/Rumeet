package com.d204.rumeet.ui.mypage

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentMyPageBinding
import com.d204.rumeet.ui.base.*
import com.d204.rumeet.ui.mypage.adapter.MyPageMenuAdapter
import com.d204.rumeet.ui.mypage.model.MyPageMenuUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_my_page
    override val viewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage) { defaultViewModelProviderFactory }

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
        initMenu()
    }

    override fun initDataBinding() {
        initMyPageAction()
        viewModel.getUserId()
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.userId.collect {
                    viewModel.getUserInfo()
                }
            }
            launch {
                viewModel.userInfo.collect() {
                    initText()
                    initProfile()
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    private fun initProfile() {
        val profile =
            viewModel.userInfo.value.successOrNull()?.profile ?: R.drawable.ic_app_main_logo
        Glide.with(requireContext()).load(profile).override(50, 50).into(binding.ivMypageProfile)
    }

    private fun initText() {
        val userName = viewModel.userInfo.value.successOrNull()?.nickname ?: ""
        Log.d("TAG", "initText: $userName")
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
        viewModel.setMyPageMunuTitleList(menuTitleList)

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

        val myPageMenuAdapter = MyPageMenuAdapter(viewModel).apply {
            submitList(menuList)
        }
        with(binding.rvMypageMenu) {
            layoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            adapter = myPageMenuAdapter
            bringToFront()
        }
    }

    private fun initMyPageAction() {
        binding.vm = viewModel
        lifecycleScope.launchWhenResumed {
            viewModel.myPageNavigationEvent.collectLatest { action ->
                when (action) {
                    MyPageAction.BadgeList -> navigate(MyPageFragmentDirections.actionMyPageFragmentToBadgeListFragment())
                    MyPageAction.EditProfile -> navigate(
                        MyPageFragmentDirections.actionMyPageFragmentToJoinNicknameFragment2(
                            1,
                            viewModel.userInfo.value.successOrNull()?.profile ?: "",
                            true,
                            viewModel.userInfo.value.successOrNull()?.nickname ?: "",
                            viewModel.userId.value.successOrNull() ?: -1
                        )
                    )
                    MyPageAction.FriendList -> navigate(MyPageFragmentDirections.actionMyPageFragmentToFriendListFragment())
                    MyPageAction.LogOut -> showLogoutDialog()
                    MyPageAction.MatchingHistory -> navigate(MyPageFragmentDirections.actionMyPageFragmentToMatchingHistoryFragment())
                    MyPageAction.RunningRecord -> navigate(MyPageFragmentDirections.actionMyPageFragmentToRunningRecordFragment())
                    MyPageAction.Setting -> navigate(MyPageFragmentDirections.actionMyPageFragmentToSettingFragment())
                }
            }
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
            setLogoutState(true, viewModel)
        }
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }
}