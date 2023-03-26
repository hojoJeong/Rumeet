package com.d204.rumeet.bindindAdapters

import androidx.databinding.BindingAdapter
import com.d204.rumeet.ui.components.FilledEditText

@BindingAdapter("isEnable")
fun FilledEditText.bindIsEnable(state : Boolean){
    binding.editInput.isEnabled = state
}