package com.d204.rumeet.ui.mypage

import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentBadgeListBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.mypage.adapter.BadgeContentListAdapter
import com.d204.rumeet.ui.mypage.model.BadgeDetailUiModel
import com.d204.rumeet.ui.mypage.model.BadgeContentListUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadgeListFragment : BaseFragment<FragmentBadgeListBinding, BaseViewModel>() {
    private val myPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage)
    override val layoutResourceId: Int
        get() = R.layout.fragment_badge_list
    override val viewModel: BaseViewModel
        get() = myPageViewModel

    override fun initStartView() {
        initBadgeList()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initBadgeList() {
        //TODO 뱃지 서버통신
        val myBadge = listOf<BadgeContentListUiModel>(
            BadgeContentListUiModel(
                "제목",
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                ),
            ),
            BadgeContentListUiModel(
                "제목",
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
                )
            ),
            BadgeContentListUiModel(
                "제목",
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
                )            ),
            BadgeContentListUiModel(
                "제목",
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
                )            ),
            BadgeContentListUiModel(
                "제목",
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
                )            ),
        )

        val recentBadge =
            BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
        binding.recent = recentBadge

        val badgeListAdapter = BadgeContentListAdapter().apply {
            submitList(myBadge)
        }
        with(binding.rvBadgeContentList) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = badgeListAdapter
        }

    }

    private fun initView() {
    }

}