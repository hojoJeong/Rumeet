package com.d204.rumeet.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentSignUpEditTextBinding
import com.d204.rumeet.databinding.ContentSingleTextButtonBinding

class SingleButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var _binding: ContentSingleTextButtonBinding? = null
    val binding: ContentSingleTextButtonBinding get() = _binding!!

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.content_single_text_button,
            this,
            false
        )
        addView(binding.root)
    }

    fun setContent(content: String) {
        binding.btnSingleText.text = content
    }

    fun setState(state: Boolean) {
        with(binding.btnSingleText) {
            if (state) {
                isEnabled = true
                setBackgroundResource(R.drawable.bg_rect_transparent_navyblue_round8_stroke0)
            } else {
                isEnabled = false
                setBackgroundResource(R.drawable.bg_rect_transparent_light_grey_round8_stroke0)
            }
        }
    }

    fun addClickListener(listener: OnClickListener?){
        binding.btnSingleText.setOnClickListener(listener)
    }

    fun setBtnBackGround(img: Int){
        binding.btnSingleText.setBackgroundResource(img)
    }
}