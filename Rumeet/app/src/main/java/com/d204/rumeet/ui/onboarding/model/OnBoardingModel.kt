package com.d204.rumeet.ui.onboarding.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnBoardingModel(
    val img: Int,
    val description: String
) : Parcelable
