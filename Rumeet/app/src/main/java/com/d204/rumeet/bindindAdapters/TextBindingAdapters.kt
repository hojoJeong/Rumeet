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
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.util.toRecord

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
    val name = userName.successOrNull() ?: ""
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

@BindingAdapter(value = ["recordValue", "recordTitle"])
fun TextView.setBestRecord(recordValue: UiState<List<BestRecordUiModel>>, recordTitle: String) {
    val value = if(recordTitle == "누적 횟수") recordValue.successOrNull()?.get(0)?.value
    else if(recordTitle == "누적 거리") recordValue.successOrNull()?.get(1)?.value
    else recordValue.successOrNull()?.get(2)?.value
    val checkString = value?.substring(value.length - 1)
    text = when (checkString) {
        "m" -> {
            SpannableStringBuilder(value).apply {
                setSpan(
                    RelativeSizeSpan(0.7f),
                    value.length - 2,
                    value.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        "회" -> {
            SpannableStringBuilder(value).apply {
                setSpan(
                    RelativeSizeSpan(0.7f),
                    value.length - 1,
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

@BindingAdapter(value = ["badgeUrl","badgeTitle"])
fun TextView.setBadgeText(url: String, title: String){
    text = title
    if(url.substring(url.length - 6) == "no.png"){
        this.setTextColor(context.getColor(R.color.nobel))
    } else {
        this.setTextColor(context.getColor(R.color.black))
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
    when(mode.substring(0, 2)){
        "싱글" -> {
            setTextColor(context.getColor(R.color.navy_blue))
            text = mode
        }
        "경쟁"-> {
            setTextColor(context.getColor(R.color.red))
            text = mode
        }
        "협동" -> {
            setTextColor(context.getColor(R.color.dandelion))
            text = mode
        }
    }
}

@BindingAdapter("setSuccess")
fun TextView.setSuccess(success: String) {
    if(success == "승리") {
        setTextColor(context.getColor(R.color.navy_blue))
    } else {
        setTextColor(context.getColor(R.color.red))
    }
    text = success
}

@BindingAdapter("setActivityTitle")
fun TextView.setActivityTitle(index: String) {
    text = "나의 ${index}째 러닝"
}

@BindingAdapter("setPartnerName")
fun TextView.setPartnerName(name: String){
    val content = "vs $name"
    text = SpannableStringBuilder(content).apply {
        setSpan(
            RelativeSizeSpan(0.7f),
            0,
            2,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

}
