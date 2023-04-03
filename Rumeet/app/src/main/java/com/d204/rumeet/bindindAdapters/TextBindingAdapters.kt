package com.d204.rumeet.bindindAdapters

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.d204.rumeet.R
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.FilledEditText
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@BindingAdapter("authentication_visibility")
fun TextView.bindAuthenticationVisibility(state: Boolean) {
    if (state) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("chatting_date")
fun TextView.bindChattingDate(date : Long){
    //오늘 = 시간
    val timeSimpleDateFormat = SimpleDateFormat("hh:mm", Locale.KOREA)
    // 사흘부터
    val dateSimpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)

    val today = LocalDate.now(ZoneId.systemDefault())
    val yesterday = today.minusDays(1)

    val time = Instant.ofEpochMilli(date)
    val dateToCheck = time.atZone(ZoneId.systemDefault()).toLocalDate()

    val isToday = dateToCheck == today
    val isYesterday = dateToCheck == yesterday

    text = when {
        isToday -> timeSimpleDateFormat.format(date)
        isYesterday -> "어제"
        else -> dateSimpleDateFormat.format(date)
    }
}

@BindingAdapter("time")
fun TextView.bindTime(date : Long){
    val timeSimpleDateFormat = SimpleDateFormat("yyyy:MM:dd:hh:mm", Locale.KOREA)
    text = timeSimpleDateFormat.format(date).substring(11)
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

@BindingAdapter("set_integer")
fun TextView.setInteger(value : Int){
    if(value != 0) {
        text = value.toString()
        visibility = View.VISIBLE
    } else{
        visibility = View.GONE
    }
}

@BindingAdapter("setMode")
fun TextView.setMode(mode: String) {
    //TODO 운동기록과 매칭 기록에서 모드,승패 여부에 따라 text 처리
}