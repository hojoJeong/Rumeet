package com.d204.rumeet.bindindAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.d204.rumeet.R


object ImageBindingAdapters {
    @JvmStatic
    @BindingAdapter("imgByUrl")
    fun ImageView.bindImageViewByUrl(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("setImageByGlide")
    fun ImageView.setImageByGlide(url: String) {
        Glide.with(context).load(url).into(this)
    }

    @JvmStatic
    @BindingAdapter("setImageResource")
    fun ImageView.setImageResource(img: Int) {
        setImageResource(img)
    }
}
