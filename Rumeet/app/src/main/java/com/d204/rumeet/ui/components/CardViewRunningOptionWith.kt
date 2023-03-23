package com.d204.rumeet.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentRunningOptionDistanceBinding
import com.d204.rumeet.databinding.ContentRunningOptionWithBinding

class CardViewRunningOptionWith @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var _binding: ContentRunningOptionWithBinding? = null
    val binding: ContentRunningOptionWithBinding
        get() = _binding!!

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.content_running_option_with,
            this,
            false
        )
        addView(binding.root)
    }
}