package com.d204.rumeet.ui.mypage

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.d204.rumeet.R
import com.d204.rumeet.bindindAdapters.ImageBindingAdapters.setImageByGlide
import com.d204.rumeet.databinding.DialogMapBinding

private const val TAG = "러밋_MapDialog"
class MapDialog(
    context: Context,
    message: String,
    polyline: String,
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogMapBinding? = null
    private val binding get() = _binding!!

    private var id: String? = null
    private var message: String? = null
    private var polyline: String?= null

    init {
        this.id = id
        this.message = message
        this.polyline = polyline
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_map, container, false)
        binding.dialog = this
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.message = message
        binding.polyline = polyline

        return view
    }

    fun onOkButtonClick(view: View) {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}