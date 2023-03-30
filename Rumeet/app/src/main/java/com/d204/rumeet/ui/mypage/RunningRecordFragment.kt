package com.d204.rumeet.ui.mypage

import androidx.navigation.navGraphViewModels
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningRecordBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.util.toDate

class RunningRecordFragment : BaseFragment<FragmentRunningRecordBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_record
    override val viewModel: MyPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage){defaultViewModelProviderFactory}

    override fun initStartView() {
        initDatePicker()
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {
    }

    private fun initDatePicker(){
        var sFirstUse = true
        var eFirstUse = true
        binding.btnRunningRecordStartDate.setOnClickListener {
            showDatePickerDialog("시작",sFirstUse, binding)
            sFirstUse = false
        }
        binding.btnRunningRecordEndDate.setOnClickListener {
            showDatePickerDialog("종료",eFirstUse, binding)
            eFirstUse = false
        }
    }

    private fun showDatePickerDialog(title: String, firstUse: Boolean, parentBinding: FragmentRunningRecordBinding) {
        val initDate = if(firstUse){
            System.currentTimeMillis()
        } else{
            if(title == "시작") binding.tvRunningRecordStartDate.text.toString().toDate()
            else binding.tvRunningRecordEndDate.text.toString().toDate()
        }
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(
                title = "$title 날짜 설정",
                content = "",
                buttonText = "확인"
            )
        ).apply {
            setInitDatePickerData(true, initDate,title, parentBinding)
        }
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }
}