package com.d204.rumeet.util

import android.view.View
import java.util.concurrent.atomic.AtomicBoolean

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private val intervalMs: Long = 1500
) : View.OnClickListener {
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        // 현재 value를 return 후 newValue로 업데이트
        if (canClick.getAndSet(false)) {
            v?.run {
                // intervalMs 마다 클릭가능하도록 변경
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.onClick(v)
            }
        }
    }
}