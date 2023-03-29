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
                getString(R.string.title_badge_welcome),
                listOf(
                    BadgeDetailUiModel(resources.getStringArray(R.array.url_badge)[], resources.getStringArray(R.array.title_badge), "", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_first_victory_no, "웰컴", "2023.03.03", "설명설명"),
                ),
            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_distance),
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_distance_100_no, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_distance_500_no, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_distance_1000_no, "웰컴", "2023.03.03", "설명설명")
                )
            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_competition),
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
                )            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_team),
                listOf(
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명"),
                    BadgeDetailUiModel(R.drawable.ic_badge_welcome, "웰컴", "2023.03.03", "설명설명")
                )            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_relay),
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