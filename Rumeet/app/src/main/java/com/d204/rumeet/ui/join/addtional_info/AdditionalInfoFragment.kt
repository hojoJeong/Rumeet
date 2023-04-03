package com.d204.rumeet.ui.join.addtional_info

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentAddtionalInfoBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.BodyInfoBottomSheetDialog
import com.d204.rumeet.ui.join.JoinViewModel
import com.d204.rumeet.ui.mypage.MyPageViewModel

import kotlinx.coroutines.flow.collectLatest

class AdditionalInfoFragment : BaseFragment<FragmentAddtionalInfoBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_addtional_info
    override val viewModel: JoinViewModel by activityViewModels()
    private var gedner = -1
    private val args by navArgs<AdditionalInfoFragmentArgs>()
    val myPageViewModel: MyPageViewModel by navGraphViewModels(R.id.navigation_mypage) { defaultViewModelProviderFactory }


    override fun initStartView() {
        with(binding) {
            vm = viewModel
            socialType = viewModel.joinInfo.socialJoinModel != null
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
        initViewWhenReset()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.additionalInfoAction.collectLatest {
                when (it) {
                    is AdditionalInfoAction.SocialSignUp -> {
                        if (checkEmptyValue()) {
                            if (!args.reset) {
                                viewModel.socialSignUp()
                            } else {
                                viewModel.editUserInfo.id = myPageViewModel.userId.value.successOrNull()!!
                                viewModel.editUserInfo()
                            }
                        } else showSignUpFailedDialog()
                    }
                    is AdditionalInfoAction.EmailSignUp -> {
                        if (checkEmptyValue()) {
                            if (!args.reset) {
                                viewModel.emailSignUp()
                            } else {
                                viewModel.editUserInfo.id = myPageViewModel.userId.value.successOrNull()!!
                                viewModel.editUserInfo()
                            }
                        } else showSignUpFailedDialog()
                    }
                    is AdditionalInfoAction.SignUpSuccess -> {
                        if (!args.reset) {
                            toastMessage("회원가입이 성공했습니다.")
                            navigate(AdditionalInfoFragmentDirections.actionAdditionalInfoFragmentToLoginFragment())
                        } else {
                            toastMessage("정보 수정이 완료되었습니다.")
                            myPageViewModel.getUserInfo()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        if (args.reset) {
            binding.btnRumeet.setContent("정보 수정")
        } else {
            binding.btnRumeet.setContent("계속하기")
        }
        binding.tvBodyState.setOnClickListener {
            showBodyStateDialog()
        }
        binding.rgGender.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.btn_male -> {
                    gedner = 0
                }
                R.id.btn_female -> {
                    gedner = 1
                }
            }
        }
    }

    private fun initViewWhenReset(){
        if(args.reset){
            with(binding){
                tvAdditionalInfoTitle.visibility = View.GONE
                tvAdditionalInfoDescription.visibility = View.GONE
            }
        }
    }

    private fun showBodyStateDialog() {
        val dialog = BodyInfoBottomSheetDialog().apply {
            addButtonClickListener { tallValue, weightValue ->
                if (!args.reset) {
                    viewModel.joinInfo.height = tallValue.toFloat()
                    viewModel.joinInfo.weight = weightValue.toFloat()
                } else {
                    viewModel.editUserInfo.height = tallValue.toFloat()
                    viewModel.editUserInfo.weight = weightValue.toFloat()
                }
                initPreviousData(viewModel.joinInfo.height, viewModel.joinInfo.weight)
                dismissAllowingStateLoss()
                this@AdditionalInfoFragment.binding.tvBodyState.text =
                    "${tallValue}cm / ${weightValue}kg"
            }
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun checkEmptyValue(): Boolean {
        return binding.let {
            if (this@AdditionalInfoFragment.gedner != -1 && it.tvBodyState.text.isNotEmpty() && it.editBirth.text.isNotEmpty()) {
                if (!args.reset) {
                    viewModel.joinInfo.age = it.editBirth.text.toString().toInt()
                    viewModel.joinInfo.gender = gedner
                } else {
                    viewModel.editUserInfo.age = it.editBirth.text.toString().toInt()
                    viewModel.editUserInfo.gender = gedner
                }
                true
            } else {
                false
            }
        }
    }

    private fun showSignUpFailedDialog() {
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(title = "알림 메시지", content = "빈칸을 모두 채워주세요", buttonText = "확인")
        )
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }
}