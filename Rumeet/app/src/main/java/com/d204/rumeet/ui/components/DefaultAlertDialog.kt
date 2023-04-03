package com.d204.rumeet.ui.base

import android.view.View
import androidx.databinding.BindingAdapter
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentSingleButtonDialogBinding
import com.d204.rumeet.util.OnSingleClickListener

data class AlertModel(
    val title: String,
    val content: String,
    val buttonText: String
)

class DefaultAlertDialog(
    private val alertModel: AlertModel,
    private val buttonListener: (() -> Unit)? = null
) : BaseDialogFragment<ContentSingleButtonDialogBinding>(layoutId = R.layout.content_single_button_dialog) {
    override val layoutResourceId: Int
        get() = R.layout.content_single_button_dialog

    private var cancelButtonVisibility = false

    override fun initStartView() {
        binding.cancelState = cancelButtonVisibility
        binding.alertModel = alertModel
        with(binding.btnOkay) {
            buttonListener?.also {
                setOnClickListener {
                    buttonListener.invoke()
                    dismissAllowingStateLoss()
                }
            } ?: setOnClickListener { dismissAllowingStateLoss() }
        }
        binding.btnCancel.setOnClickListener { dismissAllowingStateLoss() }
    }

    override fun initDataBinding() {}

    override fun initAfterBinding() {}

    fun setCancelButtonVisibility(state: Boolean) {
        cancelButtonVisibility = state
    }
}