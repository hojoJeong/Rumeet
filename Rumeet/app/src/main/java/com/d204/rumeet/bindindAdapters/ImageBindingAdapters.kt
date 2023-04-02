package com.d204.rumeet.bindindAdapters

import android.icu.text.Transliterator.Position
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.ui.base.UiState
import com.d204.rumeet.ui.base.successOrNull


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
    fun ImageView.setImageByGlide(url: String?) {
        Glide.with(context).load(url).into(this)
    }

    @JvmStatic
    @BindingAdapter("setImageResource")
    fun ImageView.setImageResource(img: Int) {
        setImageResource(img)
    }

    @JvmStatic
    @BindingAdapter(value = ["setBadgeImg", "position"])
    fun ImageView.setBadgeImg(badgeList: UiState<List<String>>, position: Int){
        when(position){
            1 -> {
                Glide.with(context).load(badgeList.successOrNull()?.get(0)).into(this)
            }
            2-> {
                Glide.with(context).load(badgeList.successOrNull()?.get(1)).into(this)
            }
//            3 -> {
//                Glide.with(context).load(badgeList.successOrNull()?.get(2)).into(this)
//            }
        }
    }
}
