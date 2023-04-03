package com.d204.rumeet

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_KEY))

    }
}