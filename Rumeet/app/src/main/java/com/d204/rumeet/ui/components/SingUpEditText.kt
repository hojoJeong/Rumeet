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
import com.d204.rumeet.databinding.ContentSingleLineEditTextBinding
import com.d204.rumeet.util.getColorWithNoTheme
import com.d204.rumeet.util.setTextColorWithNoTheme

class SingUpEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var _binding: ContentSingleLineEditTextBinding? = null
    val binding: ContentSingleLineEditTextBinding get() = _binding!!

    val keyword: String get() = binding.editInput.text.toString()
    val idValidate: Boolean get() = checkIdValidation()
    val passwordValidate: Boolean get() = checkPasswordValidate()
    val nicknameValidate: Boolean get() = checkNickNameValidate()

    enum class SingleLineEditTextType {
        ID, PASSWORD, NORMAL
    }

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.content_single_line_edit_text,
            this,
            false
        )
        addView(binding.root)
    }

    fun setEditTextType(type: SingleLineEditTextType, hintText: String = "") {
        when (type) {
            SingleLineEditTextType.ID -> {
                setIdInput()
                addTextWatcher()
            }
            SingleLineEditTextType.PASSWORD -> {
                setPasswordInput()
                addTextWatcher()
            }
            SingleLineEditTextType.NORMAL -> {

            }
        }
        setHint(hintText)
    }

    private fun checkIdValidation(): Boolean {
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        return if (pattern.matcher(keyword).matches()) {
            true
        } else {
            setStateMessage(context.getString(R.string.content_id_email_error), false)
            false
        }
    }

    private fun checkNickNameValidate(): Boolean {
        return if (binding.editInput.text.length > 12) {
            setStateMessage(context.getString(R.string.content_nickname_size_error), false)
            false
        } else {
            true
        }
    }

    private fun checkPasswordValidate(): Boolean {
        val regexComplexPassword =
            Regex("^.*(?=^.{8,15}\$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#\$%^&+=]).*\$")
        return if (keyword.matches(regexComplexPassword)) {
            setStateMessage(context.getString(R.string.content_password_validate_pass), true)
            true
        } else {
            setStateMessage(context.getString(R.string.content_password_validate_error), false)
            false
        }
    }

    fun checkPasswordMatch(password: String): Boolean {
        return if (passwordValidate && keyword == password) {
            setStateMessage(context.getString(R.string.content_password_check), true)
            true
        } else {
            setStateMessage(context.getString(R.string.content_password_check_error), false)
            false
        }
    }

    fun setStateMessage(message: String, state: Boolean) {
        if (state) {
            with(binding.tvEditState) {
                setTextColorWithNoTheme(R.color.kelly_green)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_validate_pass, 0, 0, 0)
                text = message
            }
            binding.divEdit.setBackgroundColor(context.getColorWithNoTheme(R.color.kelly_green))
        } else {
            with(binding.tvEditState) {
                setTextColorWithNoTheme(R.color.red)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_validate_error, 0, 0, 0)
                text = message
            }
            binding.divEdit.setBackgroundColor(context.getColorWithNoTheme(R.color.red))
        }
    }

    private fun setHint(hintText: String) {
        binding.editInput.hint = hintText
    }

    private fun setSingleLineInput() {
        with(binding.editInput) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            maxLines = 1
            maxEms = 30
        }
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