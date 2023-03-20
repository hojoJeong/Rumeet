package com.d204.rumeet.ui.components

import android.content.Context
import android.provider.Telephony.Carriers.PASSWORD
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentFilledEditTextBinding
import java.lang.reflect.Field

class FilledEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var _binding: ContentFilledEditTextBinding? = null
    val binding: ContentFilledEditTextBinding get() = _binding!!

    val inputText get() = binding

    enum class FilledEditTextType{
        ID, PASSWORD, NORMAL
    }

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.content_filled_edit_text,
            this,
            false
        )

        addView(binding.root)
    }

    fun setEditTextType(type : FilledEditTextType){
        when(type){
            FilledEditTextType.ID -> {}
            FilledEditTextType.PASSWORD -> {}
            FilledEditTextType.NORMAL -> {}
        }
    }

    fun setHint(hintText : String){

    }

    fun addTextDeleteButton(){

    }

    fun addPasswordVisibilityButton(){

    }
}