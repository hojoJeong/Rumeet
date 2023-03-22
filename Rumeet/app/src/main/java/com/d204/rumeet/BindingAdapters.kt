package com.d204.rumeet

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d204.rumeet.home.adapter.BestRecordAdapter
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.home.model.BestRecordUiModel

object BindingAdapters {
    @JvmStatic
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

    @JvmStatic
    @BindingAdapter("setBestRecord")
    fun TextView.setBestRecord(value: String) {
        val checkString = value.substring(value.length - 2)
        text = when (checkString) {
            "km" -> {
                SpannableStringBuilder(value).apply {
                    setSpan(
                        RelativeSizeSpan(0.7f),
                        value.length - 2,
                        value.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            "al" -> {
                SpannableStringBuilder(value).apply {
                    setSpan(
                        RelativeSizeSpan(0.7f),
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

    @JvmStatic
    @BindingAdapter("setBestRecordState")
    fun View.setBestRecordState(uiState: UiState<List<BestRecordUiModel>>){
        rootView.findViewById<TextView>(R.id.tv_content_home_title).text = context.getString(R.string.title_best_record)
        if(uiState.successOrNull()!!.isEmpty()){
            with(rootView.findViewById<TextView>(R.id.tv_content_home_message)){
                text = context.getString(R.string.content_no_best_record)
                visibility = View.VISIBLE
            }
            rootView.findViewById<TextView>(R.id.btn_content_home).visibility = View.GONE
        } else {
            rootView.findViewById<RecyclerView>(R.id.rv_content_home).adapter
        }
    }


    @JvmStatic
    @BindingAdapter("setImageByGlide")
    fun ImageView.setImageByGlide(url: String) {
        Glide.with(context).load(url).into(this)
    }
}