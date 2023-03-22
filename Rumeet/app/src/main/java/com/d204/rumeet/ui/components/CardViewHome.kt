package com.d204.rumeet.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentHomeCardviewBinding

class CardViewHome @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var _binding: ContentHomeCardviewBinding? = null
    val binding: ContentHomeCardviewBinding
        get() = _binding!!

    init {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.content_home_cardview, this, false
        )

        addView(binding.root)
    }

    fun setViewWhenEmptyData(title: String, initMessage: String) {
        binding.tvContentHomeTitle.text = title
        setButton(true, null, null)
        setRecyclerView(true, null)
        setMessage(true, initMessage)
    }

    fun setViewContent(title: String, btnImg: Drawable?) {
        Log.d("카드", "setViewContent: $title")
        binding.tvContentHomeTitle.text = title
        setMessage(false, null)

        when(title){
            context.getString(R.string.title_best_record) -> {
                setButton(false, null, null)
                setRecyclerView(true, "horizontal")
            }
            context.getString(R.string.title_my_badge) -> {
                setButton(true, context.getString(R.string.title_see_more), btnImg)
                setRecyclerView(true, "horizontal")
            }

            context.getString(R.string.title_recommend_random_friend) -> {
                setButton(true, context.getString(R.string.title_refresh), btnImg)
                setRecyclerView(true, "vertical")
            }
        }
    }

    private fun setButton(visible: Boolean, title: String?, img: Drawable?) {
        if (visible) {
            with(binding.btnContentHome) {
                visibility = View.VISIBLE
                text = title
                setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
            }
        } else {
            binding.btnContentHome.visibility = View.GONE
        }
    }

    private fun setRecyclerView(visible: Boolean, layoutManager: String?) {
        if (visible) {
            with(binding.rvContentHome) {
                visibility = View.VISIBLE

                if (layoutManager == "vertical") {
                    this.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                } else {
                    this.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        } else {
            binding.rvContentHome.visibility = View.GONE
        }
    }

    private fun setMessage(visible: Boolean, message: String?) {
        if (visible) {
            with(binding.tvContentHomeMessage) {
                visibility = View.VISIBLE
                text = message
            }
        } else {
            binding.tvContentHomeMessage.visibility = View.GONE
        }
    }

}