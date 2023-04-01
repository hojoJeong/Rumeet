package com.d204.rumeet.bindindAdapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.components.FilledEditText
import com.d204.rumeet.ui.home.model.BestRecordUiModel
import com.d204.rumeet.ui.home.model.RecommendFriendUiModel

@BindingAdapter("isEnable")
fun FilledEditText.bindIsEnable(state : Boolean){
    binding.editInput.isEnabled = state
}