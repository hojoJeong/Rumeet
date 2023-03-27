package com.d204.rumeet.ui.base

import android.view.View
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentSingleButtonDialogBinding
import com.d204.rumeet.util.OnSingleClickListener

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

    override fun initStartView() {
        binding.alertModel = alertModel
        binding.btnOkay.setOnClickListener { dismissAllowingStateLoss() }
    }

    override fun initDataBinding() {}

    override fun initAfterBinding() {}
}