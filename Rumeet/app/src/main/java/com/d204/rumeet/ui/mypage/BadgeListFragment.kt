package com.d204.rumeet.ui.mypage

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentBadgeListBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.adapter.BadgeContentListAdapter
import com.d204.rumeet.ui.mypage.model.AcquiredBadgeUiModel
import com.d204.rumeet.ui.mypage.model.BadgeDetailUiModel
import com.d204.rumeet.ui.mypage.model.BadgeContentListUiModel
import com.d204.rumeet.util.toDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BadgeListFragment : BaseFragment<FragmentBadgeListBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_badge_list
    override val viewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage) { defaultViewModelProviderFactory }


    override fun initStartView() {
        viewModel.getAcquiredBadgeList()

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.acquiredBadgeList.collect {
                    initRecentBadge(it.successOrNull() ?: emptyList())
                    initBadgeList(it.successOrNull() ?: emptyList())
                    if(it.successOrNull()?.size?:0 > 0){
                        binding.tvBadgeRecentNo.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initRecentBadge(badgeList: List<AcquiredBadgeUiModel>) {
        if (badgeList.isEmpty()) {
            binding.tvBadgeRecentNo.visibility = View.VISIBLE
            binding.tvBadgeRecentDate.visibility = View.GONE
        } else {
            var recentBadgeDate = 0L
            var recentBadgeName = ""
            var recentBadgeImg = ""
            val badgeNameList = resources.getStringArray(R.array.title_badge)
            val badgeCodeList = resources.getStringArray(R.array.code_badge)
            val badgeImgList = resources.getStringArray(R.array.url_badge)
            for (badge in badgeList) {
                if (recentBadgeDate < badge.date) {
                    recentBadgeDate = badge.date
                    recentBadgeName = badgeNameList[badgeCodeList.indexOf(badge.code.toString())]
                    recentBadgeImg = badgeImgList[badgeCodeList.indexOf(badge.code.toString())]
                }
            }

            val recentBadge =
                BadgeDetailUiModel(recentBadgeImg, recentBadgeName, recentBadgeDate.toDate())

            Log.d(TAG, "initRecentBadge: $recentBadge")
            binding.recent = recentBadge
            Log.d(TAG, "initRecentBadge binding recent: ${binding.recent}")
        }
    }

    private fun initBadgeList(acquiredBadgeList: List<AcquiredBadgeUiModel>) {
        val myBadge = listOf<BadgeContentListUiModel>(
            BadgeContentListUiModel(
                getString(R.string.title_badge_welcome),
                listOf(
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[33],
                        resources.getStringArray(R.array.title_badge)[32],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[13],
                        resources.getStringArray(R.array.title_badge)[12],
                        ""
                    ),
                ),
            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_distance),
                listOf(
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[7],
                        resources.getStringArray(R.array.title_badge)[6],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[9],
                        resources.getStringArray(R.array.title_badge)[8],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[11],
                        resources.getStringArray(R.array.title_badge)[10],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[15],
                        resources.getStringArray(R.array.title_badge)[14],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[17],
                        resources.getStringArray(R.array.title_badge)[16],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[19],
                        resources.getStringArray(R.array.title_badge)[18],
                        ""
                    ),
                )
            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_competition),
                listOf(
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[1],
                        resources.getStringArray(R.array.title_badge)[0],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[3],
                        resources.getStringArray(R.array.title_badge)[2],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[5],
                        resources.getStringArray(R.array.title_badge)[4],
                        ""
                    ),
                )
            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_team),
                listOf(
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[27],
                        resources.getStringArray(R.array.title_badge)[26],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[29],
                        resources.getStringArray(R.array.title_badge)[28],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[31],
                        resources.getStringArray(R.array.title_badge)[30],
                        ""
                    ),
                )
            ),
            BadgeContentListUiModel(
                getString(R.string.title_badge_relay),
                listOf(
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[21],
                        resources.getStringArray(R.array.title_badge)[20],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[23],
                        resources.getStringArray(R.array.title_badge)[22],
                        ""
                    ),
                    BadgeDetailUiModel(
                        resources.getStringArray(R.array.url_badge)[25],
                        resources.getStringArray(R.array.title_badge)[24],
                        ""
                    ),
                )
            ),
        )

        for (acquiredBadge in acquiredBadgeList) {
            val index = acquiredBadge.code.toString()
            val curBadge =
                if (index.length == 1) {
                    myBadge[0].badgeList[Character.getNumericValue(
                        index[0]
                    )]
                } else {
                    myBadge[Character.getNumericValue(index[0])].badgeList[Character.getNumericValue(
                        index[1]
                    )]
                }

            with(curBadge) {
                badgeDate = acquiredBadge.date.toDate()
                val curUrl = resources.getStringArray(R.array.url_badge)
                badgeImg = curUrl[curUrl.indexOf(badgeImg) - 1]
            }
        }

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