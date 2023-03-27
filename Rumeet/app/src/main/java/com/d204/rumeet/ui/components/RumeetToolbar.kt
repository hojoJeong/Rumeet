package com.d204.rumeet.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentToolbarBinding

class RumeetToolbar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var _binding: ContentToolbarBinding? = null
    val binding: ContentToolbarBinding get() = _binding!!

    enum class ToolbarType {
        LOGO_TEXT_ALARM, BACK_TEXT, TEXT
    }

    enum class ToolbarLeftImgType {
        LOGO, BACK
    }

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.content_toolbar,
            this,
            false
        )
        addView(binding.root)
    }

    fun setToolbarType(
        type: ToolbarType,
        title: String = "",
        leftClickListener: OnClickListener? = null,
        rightClickListener: OnClickListener? = null
    ) {
        toolbarClear()
        binding.tvTitle.text = title
        when (type) {
            ToolbarType.BACK_TEXT -> {
                setToolbarLeftImg(ToolbarLeftImgType.BACK, leftClickListener)
            }
            ToolbarType.LOGO_TEXT_ALARM -> {
                setToolbarLeftImg(ToolbarLeftImgType.LOGO, leftClickListener)
                binding.ivRightImg.setImageResource(R.drawable.ic_alarm)
                binding.ivRightImg.setOnClickListener(rightClickListener)
            }
            // basic type
            ToolbarType.TEXT -> {}
        }
    }

    private fun setToolbarLeftImg(type: ToolbarLeftImgType, leftClickListener: OnClickListener?) {
        when (type) {
            ToolbarLeftImgType.LOGO -> {
                binding.ivLeftImg.setImageResource(R.drawable.ic_app_main_logo)
            }
            ToolbarLeftImgType.BACK -> {
                binding.ivLeftImg.setImageResource(R.drawable.ic_back)
            }
        }
        binding.ivLeftImg.setOnClickListener(leftClickListener)
    }

    private fun toolbarClear() {
        with(binding) {
            ivLeftImg.setImageResource(0)
            tvTitle.text = ""
            ivRightImg.setImageResource(0)
        }
    }

}