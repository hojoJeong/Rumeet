package com.d204.rumeet.bindindAdapters

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.d204.rumeet.R
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.FilledEditText

@BindingAdapter("authentication_visibility")
fun TextView.bindAuthenticationVisibility(state: Boolean) {
    if (state) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

@BindingAdapter("setWelcomeMessage")
fun TextView.setWelcomeMessage(userName: UiState<String>) {
    val name = userName.successOrNull()!!
    val content = resources.getString(R.string.content_welcome_message, name)
    val builder = SpannableStringBuilder(content).apply {
        setSpan(
            RelativeSizeSpan(1.2f), 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.navy_blue)),
            0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    text = builder
}

@BindingAdapter("setBestRecord")
fun TextView.setBestRecord(value: String) {
    val checkString = value.substring(value.length - 2)
    text = when (checkString) {
        "km" -> {
            SpannableStringBuilder(value).apply {
                setSpan(
                    RelativeSizeSpan(0.6f),
                    value.length - 2,
                    value.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        "al" -> {
            SpannableStringBuilder(value).apply {
                setSpan(
                    RelativeSizeSpan(0.6f),
                    value.length - 4,
                    value.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        else -> {
            value
        }
    }
}

@BindingAdapter("setMode")
fun TextView.setMode(mode: String) {
    //TODO 운동기록과 매칭 기록에서 모드,승패 여부에 따라 text 처리
}