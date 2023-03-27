package com.d204.rumeet.bindindAdapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.d204.rumeet.ui.components.FilledEditText

@BindingAdapter("authentication_visibility")
fun TextView.bindAuthenticationVisibility(state : Boolean){
    if(state) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}