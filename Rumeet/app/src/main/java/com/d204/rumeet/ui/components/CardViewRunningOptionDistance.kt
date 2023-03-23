package com.d204.rumeet.ui.components

import android.content.ContentValues.TAG
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentRunningOptionDifficultyBinding
import com.d204.rumeet.databinding.ContentRunningOptionDistanceBinding

class CardViewRunningOptionDistance @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var _binding: ContentRunningOptionDistanceBinding? = null
    val binding: ContentRunningOptionDistanceBinding
        get() = _binding!!

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.content_running_option_distance,
            this,
            false
        )
        addView(binding.root)
        setLikeRadioBtn()
    }

    private fun setLikeRadioBtn(){
        with(binding.btnCompetition1km){
            android.util.Log.d(TAG, "setLikeRadioBtn: $isCheckable")
            setOnClickListener {

            }
        }

        with(binding.btnCompetition2km){
            setOnClickListener {
                isClickable = isChecked
            }
        }

        with(binding.btnCompetition3km){
            setOnClickListener {
                isClickable = isChecked
            }
        }

        with(binding.btnCompetition5km){
            setOnClickListener {
                isClickable = isChecked
            }
        }

    }
}