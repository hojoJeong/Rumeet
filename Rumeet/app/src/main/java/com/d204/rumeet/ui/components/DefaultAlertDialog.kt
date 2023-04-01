package com.d204.rumeet.ui.base

import android.view.View
import android.widget.DatePicker
import com.d204.rumeet.R
import com.d204.rumeet.databinding.ContentSingleButtonDialogBinding
import com.d204.rumeet.databinding.FragmentRunningRecordBinding
import com.d204.rumeet.ui.activities.LoginActivity
import com.d204.rumeet.ui.mypage.MyPageViewModel
import com.d204.rumeet.util.startActivityAfterClearBackStack
import com.d204.rumeet.util.toDate

data class AlertModel(
    val title: String,
    val content: String,
    val buttonText: String
)

class DefaultAlertDialog(
    private val alertModel: AlertModel
) : BaseDialogFragment<ContentSingleButtonDialogBinding>(layoutId = R.layout.content_single_button_dialog) {
    override val layoutResourceId: Int
        get() = R.layout.content_single_button_dialog

    private var cancelButtonVisibility = false
    private var logoutState = false
    private var datePickerVisibility = false
    private var initDate: Long = 0
    private var isStartDate = ""
    private lateinit var parentBinding: FragmentRunningRecordBinding
    private lateinit var viewModel: MyPageViewModel

    private lateinit var myPageViewModel: MyPageViewModel

    override fun initStartView() {
        binding.cancelState = cancelButtonVisibility
        binding.alertModel = alertModel
        binding.btnOkay.setOnClickListener { dismissAllowingStateLoss() }
        binding.btnCancel.setOnClickListener { dismissAllowingStateLoss() }

        if (logoutState) {
            addLogoutBtnClickListener()
        }

        if (datePickerVisibility) {
            setDatePickerVisibility(datePickerVisibility)
            initDatePicker(initDate)
            addDateBtnListener(isStartDate, parentBinding)
        }
    }

    override fun initDataBinding() {}

    override fun initAfterBinding() {}

    fun setCancelButtonVisibility(state: Boolean) {
        cancelButtonVisibility = state
    }

    private fun setDatePickerVisibility(state: Boolean) {
        if (state) {
            binding.tvDialogContent.visibility = View.GONE
            binding.dpDialog.visibility = View.VISIBLE
        }
    }

    fun setLogoutState(state: Boolean, viewModel: MyPageViewModel) {
        logoutState = state
        myPageViewModel = viewModel
    }

    private fun addLogoutBtnClickListener() {
        binding.btnOkay.setOnClickListener {
            myPageViewModel.logout()
            requireActivity().startActivityAfterClearBackStack(LoginActivity::class.java)
        }
    }

    fun setViewModel(viewModel: MyPageViewModel){
        this.viewModel = viewModel
    }

    fun setInitDatePickerData(
        state: Boolean,
        initDate: Long,
        isStartDate: String,
        parentBinding: FragmentRunningRecordBinding
    ) {
        datePickerVisibility = state
        this.initDate = initDate
        this.isStartDate = isStartDate
        this.parentBinding = parentBinding
    }

    private fun initDatePicker(initDate: Long) {
        val year = initDate.toDate().substring(0, 4).toInt()
        val month = initDate.toDate().substring(5, 7).toInt()
        val day = initDate.toDate().substring(8).toInt()
        binding.dpDialog.init(year, month, day, DatePicker.OnDateChangedListener { _, _, _, _ ->  })

    }

    private fun addDateBtnListener(
        isStartDate: String,
        parentBinding: FragmentRunningRecordBinding
    ) {
        binding.btnOkay.setOnClickListener {
            if (datePickerVisibility) {
                val year = binding.dpDialog.year
                val month = binding.dpDialog.month + 1
                val day = binding.dpDialog.dayOfMonth
                val date = "$year.$month.$day"

                val dateForLongType = date.toDate()
                //TODO 날짜 서버통신

                if (isStartDate == "시작") {
                    parentBinding.tvRunningRecordStartDate.text = date
                    val endDateText = parentBinding.tvRunningRecordEndDate.text.toString()
                    val endDate = if(endDateText == "종료 날짜") System.currentTimeMillis() else endDateText.toDate()
                    viewModel.getRunningRecord(dateForLongType, endDate)
                } else {
                    parentBinding.tvRunningRecordEndDate.text = date
                    val startDateText = parentBinding.tvRunningRecordStartDate.text.toString()
                    val startDate = if(startDateText == "시작 날짜") System.currentTimeMillis() else startDateText.toDate()
                    viewModel.getRunningRecord(startDate, dateForLongType)
                }
                binding.dpDialog.init(year, month-1, day, DatePicker.OnDateChangedListener { _, _, _, _ ->  })

                dismissAllowingStateLoss()
            }
        }
    }
}