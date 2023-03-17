package com.d204.rumeet

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("setWelcomeMessage")
    fun TextView.setWelcomeMessage(name: String){
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

    @JvmStatic
    @BindingAdapter("setImageView")
    fun ImageView.setImageView(url: String){
            Glide.with(context).load(url).into(this)
    }

    @JvmStatic
    @BindingAdapter("segt")
}