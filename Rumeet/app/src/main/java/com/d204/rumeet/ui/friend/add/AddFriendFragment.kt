package com.d204.rumeet.ui.friend.add

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentFriendAddBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.friend.add.adapter.AddFriendListAdapter
import com.d204.rumeet.ui.friend.add.model.UserListUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddFriendFragment : BaseFragment<FragmentFriendAddBinding, AddFriendViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_friend_add

    override val viewModel: AddFriendViewModel by viewModels()

    private lateinit var addUserListAdapter : AddFriendListAdapter

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.addFriendAction.collectLatest {
                when (it) {
                    is AddFriendAction.ShowUserInfoDialog -> {
                        showUserInfoDialog(it.data)
                    }
                    is AddFriendAction.SuccessRequestFriendList -> {
                        binding.lyNoResult.root.visibility =
                            if (it.listSize == 0) View.VISIBLE else View.GONE
                        hideKeyboard()
                    }
                    is AddFriendAction.AlreadyRequestFriend -> {
                        showAlreadyFriendDialog(true)
                    }
                    is AddFriendAction.AlreadyFriend -> {
                        showAlreadyFriendDialog(false)
                    }
                    is AddFriendAction.SuccessRequestFriend -> {
                        toastMessage("요청이 성공했습니다.")
                    }
                }
            }
        }
    }

    private fun showAlreadyFriendDialog(requestState : Boolean){
        val alertModel = if(requestState) AlertModel("알림 메시지", "이미 친구 요청한 상태입니다.", "확인")else
            AlertModel("알림 메시지", "이미 친구인 상태입니다.", "확인")

        val alertDialog = DefaultAlertDialog(alertModel)
        alertDialog.show(childFragmentManager, alertDialog.tag)
    }

    private fun showUserInfoDialog(userInfo: UserListUiModel) {
        val dialog = AddFriendDialog().apply {
            setUserData(userInfo)
            setRequestButtonEvent { viewModel.requestFriend(it) }
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun initAfterBinding() {
        addUserListAdapter = AddFriendListAdapter(viewModel)
        binding.rvUserList.adapter = addUserListAdapter

        binding.lyNoResult.tvContentNoResultMessage.text = "검색결과가 없습니다"
        binding.editSearchFriend.setOnEditorActionListener { v, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handled = true
                viewModel.requestSearchFriend(v.text.toString())
            }
            handled
        }
    }
}