package com.d204.rumeet

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.CardViewHome
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

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
    
    @JvmStatic
    @BindingAdapter("setBestRecordView")
    fun CardViewHome.setBestRecordView(uiState: UiState<List<BestRecordUiModel>>){
        val title = context.getString(R.string.title_best_record)
        if(uiState.successOrNull()!!.isEmpty()){
            setViewWhenEmptyData(title, context.getString(R.string.content_no_best_record))
        } else {
            setViewContent(title, null)
        }
    }

    @JvmStatic
    @BindingAdapter("setBadgeView")
    fun CardViewHome.setBadgeView(uiState: UiState<List<String>>){
        val title = context.getString(R.string.title_my_badge)
        if(uiState.successOrNull()!!.isEmpty()){
            setViewWhenEmptyData(title, context.getString(R.string.content_no_badge))
        } else {
            setViewContent(title, context.getDrawable(R.drawable.ic_arrow_right)!!)
        }
    }

    @JvmStatic
    @BindingAdapter("setRecommendFriendView")
    fun CardViewHome.setRecommendFriendView(uiState: UiState<List<RecommendFriendUiModel>>){
        val title = context.getString(R.string.title_recommend_random_friend)
        if(uiState.successOrNull()!!.isEmpty()){
            setViewWhenEmptyData(title, context.getString(R.string.content_no_pace))
        } else {
            setViewContent(title, context.getDrawable(R.drawable.ic_refresh)!!)
        }
    }

    @JvmStatic
    @BindingAdapter("setImageByGlide")
    fun ImageView.setImageByGlide(url: String) {
        Glide.with(context).load(url).into(this)
    }

    @JvmStatic
    @BindingAdapter("setImageResource")
    fun ImageView.setImageResource(img: Int){
        setImageResource(img)
    }

    @JvmStatic
    @BindingAdapter("setMode")
    fun TextView.setMode(mode: String){
        //TODO 운동기록과 매칭 기록에서 모드,승패 여부에 따라 text 처리
    }
}