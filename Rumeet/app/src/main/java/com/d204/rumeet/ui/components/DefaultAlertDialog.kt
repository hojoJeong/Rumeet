package com.d204.rumeet.ui.base

import androidx.fragment.app.viewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentSingleButtonDialogBinding
import com.d204.rumeet.ui.activities.LoginActivity
import com.d204.rumeet.ui.mypage.MyPageViewModel
import com.d204.rumeet.util.startActivityAfterClearBackStack

data class AlertModel(
    val title: String,
    val content: String,
    val buttonText: String
)

class DefaultAlertDialog(
    private val alertModel: AlertModel
) : BaseDialogFragment<ContentSingleButtonDialogBinding>(layoutId = R.layout.content_single_button_dialog) {
    override val layoutResourceId: Int
        get() = R.layout.content_single_button_dialog

    private var cancelButtonVisibility = false
    private var logoutState = false

    private lateinit var myPageViewModel: MyPageViewModel

    override fun initStartView() {
        binding.cancelState = cancelButtonVisibility
        binding.alertModel = alertModel
        binding.btnOkay.setOnClickListener { dismissAllowingStateLoss() }
        binding.btnCancel.setOnClickListener { dismissAllowingStateLoss() }
        addLogoutBtnClickListener()
    }

    override fun initDataBinding() {}

    override fun initAfterBinding() {}

    fun setCancelButtonVisibility(state: Boolean){
        cancelButtonVisibility = state
    }

    fun setLogoutState(state: Boolean, viewModel: MyPageViewModel){
        logoutState = state
        myPageViewModel = viewModel
    }

    private fun addLogoutBtnClickListener(){
        binding.btnOkay.setOnClickListener {
            myPageViewModel.logout()
            requireActivity().startActivityAfterClearBackStack(LoginActivity::class.java)
        }
    }
}