package com.d204.rumeet.bindindAdapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.d204.rumeet.ui.components.SingleButton
import com.d204.rumeet.util.OnSingleClickListener

@BindingAdapter("onSingleClick")
fun View.bindOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

@BindingAdapter("onSingleButtonClick")
fun SingleButton.bindOnBoardingButtonClick(clickListener: View.OnClickListener?){
    clickListener?.also {
        addClickListener(OnSingleClickListener(clickListener))
    } ?: addClickListener(null)
}
