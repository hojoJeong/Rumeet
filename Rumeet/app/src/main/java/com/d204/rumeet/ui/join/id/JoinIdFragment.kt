package com.d204.rumeet.ui.join.id

import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinIdBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.components.SingleLineEditText
import com.d204.rumeet.ui.join.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class JoinIdFragment : BaseFragment<FragmentJoinIdBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_id

    override val viewModel: JoinViewModel by activityViewModels()

    override fun initStartView() {
        with(binding){
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.joinIdAction.collectLatest {
                when (it) {
                    is JoinIdAction.IdDuplicate -> {
                        binding.editId.setStateMessage(getString(R.string.content_duplicated_id), false)
                    }
                    is JoinIdAction.CheckIdDuplicate -> {
                        if(binding.editId.idValidate) viewModel.idValidation(binding.editId.keyword)
                    }
                    is JoinIdAction.NavigateNicknameFragment -> {
                        navigate(JoinIdFragmentDirections.actionJoinIdFragmentToJoinNickNameFragment())
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.btnNext.setContent("계속하기")
        binding.editId.setEditTextType(SingleLineEditText.SingUpEditTextType.ID, getString(R.string.title_id_input))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(viewModel.joinInfo){
            id = ""
            password = ""
        }
    }
}