package com.d204.rumeet.ui.components

import android.widget.NumberPicker
import androidx.core.content.res.ResourcesCompat
import com.d204.rumeet.R
import com.d204.rumeet.databinding.DialogBodyInfoBinding
import com.d204.rumeet.ui.base.BaseBottomSheetDialogFragment
import okhttp3.internal.platform.android.AndroidLogHandler.setFormatter

class BodyInfoBottomSheetDialog : BaseBottomSheetDialogFragment<DialogBodyInfoBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.dialog_body_info

    private lateinit var clickEvent: (Int, Int) -> Unit

    private var tall = TALL_RANGE_VALUE / 2
    private var weight = WEIGHT_RANGE_VALUE / 2
    private val tallList = mutableListOf<Int>()
    private val weightList = mutableListOf<Int>()


    override fun initStartView() {
        initDataList()
    }

    override fun initDataBinding() {

    }

    private fun initTallPicker() {
        binding.npTall.apply {
            minValue = 0
            maxValue = TALL_RANGE_VALUE
            // init or previous value
            value = tall
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            setSelectedTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.notosanskr_medium
                )
            )
            setFormatter { value -> tallList[value].toString() }
        }
    }

    private fun initWeightPicker() {
        binding.npWeight.apply {
            minValue = 0
            maxValue = WEIGHT_RANGE_VALUE
            // init or previous value
            value = weight
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setSelectedTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.notosanskr_medium
                )
            )
            setFormatter { value -> weightList[value].toString() }
        }
    }

    override fun initAfterBinding() {
        initTallPicker()
        initWeightPicker()
        binding.btnOkay.setOnClickListener {
            clickEvent.invoke(
                binding.npTall.value + MIN_TALL_VALUE,
                binding.npWeight.value + MIN_WEIGHT_VALUE
            )
        }
    }

    fun addButtonClickListener(event: (Int, Int) -> Unit) {
        clickEvent = event
    }

    fun initPreviousData(tallIndex: Float, weightIndex: Float) {
        tall = tallIndex.toInt() - MIN_TALL_VALUE
        weight = weightIndex.toInt() - MIN_WEIGHT_VALUE
    }

    private fun initDataList() {
        for (range in MIN_TALL_VALUE..MAX_TALL_VALUE) tallList.add(range)
        for (range in MIN_WEIGHT_VALUE..MAX_WEIGHT_VALUE) weightList.add(range)
    }

    companion object {
        private const val MIN_TALL_VALUE = 140
        private const val MAX_TALL_VALUE = 200
        private const val MIN_WEIGHT_VALUE = 30
        private const val MAX_WEIGHT_VALUE = 200
        private const val TALL_RANGE_VALUE = MAX_TALL_VALUE - MIN_TALL_VALUE
        private const val WEIGHT_RANGE_VALUE = MAX_WEIGHT_VALUE - MIN_WEIGHT_VALUE
    }
}
