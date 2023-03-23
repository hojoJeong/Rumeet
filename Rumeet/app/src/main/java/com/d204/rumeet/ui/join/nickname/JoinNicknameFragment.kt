package com.d204.rumeet.ui.join.nickname

import android.graphics.Paint.Join
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinNicknameBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.components.SingUpEditText
import com.d204.rumeet.ui.join.JoinViewModel
import kotlinx.coroutines.flow.collectLatest

class JoinNicknameFragment : BaseFragment<FragmentJoinNicknameBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_nickname

    override val viewModel: JoinViewModel by activityViewModels()
    private val args by navArgs<JoinNicknameFragmentArgs>()

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        // args의 profile img가 null이면 아이디, 아니면 소셜로그인의 회원가입
        if(args.oauth != 0L){
            viewModel.joinInfo.oauth = args.oauth
            viewModel.joinInfo.profileImg = args.profileImg
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.joinNicknameAction.collectLatest {
                when (it) {
                    is JoinNicknameAction.CheckNicknameValidation -> { if(binding.editNickname.nicknameValidate) viewModel.nicknameValidation(binding.editNickname.keyword) }
                    is JoinNicknameAction.DuplicateNickname -> { binding.editNickname.setStateMessage(getString(R.string.content_duplicated_nickname), false) }
                    is JoinNicknameAction.NavigateJoinPassword -> { navigate(JoinNicknameFragmentDirections.actionJoinNickNameFragmentToJoinPasswordFragment()) }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.btnContinue.setContent("계속하기")
        binding.editNickname.setEditTextType(SingUpEditText.SingleLineEditTextType.NORMAL, getString(R.string.content_nickname_hint))
    }
}