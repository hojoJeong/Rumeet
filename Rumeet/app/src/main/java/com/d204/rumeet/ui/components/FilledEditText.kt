package com.d204.rumeet.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentFilledEditTextBinding

class FilledEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var _binding: ContentFilledEditTextBinding? = null
    val binding: ContentFilledEditTextBinding get() = _binding!!

    val inputText get() = binding.editInput.text.toString()

    enum class FilledEditTextType {
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

    fun setIsEnable(flag : Boolean){
        binding.editInput.isEnabled = flag
    }

    fun setEditTextType(type: FilledEditTextType, hintText: String = "") {
        when (type) {
            FilledEditTextType.ID -> {
                setIdInput()
                addTextWatcher()
            }
            FilledEditTextType.PASSWORD -> {
                setPasswordInput()
                addTextWatcher()
            }
            FilledEditTextType.NORMAL -> {

            }
        }
        setHint(hintText)
    }

    private fun setSingleLineInput() {
        with(binding.editInput) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            maxLines = 1
            maxEms = 30
        }
    }

    private fun setHint(hintText: String) {
        binding.editInput.hint = hintText
    }

    private fun setIdInput() {
        setSingleLineInput()
        binding.btnAdditional.setBackgroundResource(R.drawable.ic_edit_text_delete_btn)
        binding.btnAdditional.setOnClickListener {
            binding.editInput.setText("")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setPasswordInput() {
        setSingleLineInput()
        binding.btnAdditional.setBackgroundResource(R.drawable.ic_edit_text_visibilty_btn)
        binding.editInput.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.btnAdditional.setOnTouchListener { _, motionEvent ->
            // return value가 false면 추가 작업 종료, true면 추가 작업 존재함
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    binding.editInput.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                    // 커서고정
                    binding.editInput.setSelection(binding.editInput.text.toString().length)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    binding.editInput.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    // 커서이동
                    binding.editInput.setSelection(binding.editInput.text.toString().length)
                    false
                }
                else -> false
            }
        }
    }

    private fun addTextWatcher() {
        binding.editInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnAdditional.visibility = if (p0?.length?.compareTo(0) == 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}