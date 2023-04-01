package com.d204.rumeet.ui.mypage

import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningRecordBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.base.successOrNull
import com.d204.rumeet.ui.mypage.adapter.RunningActivityListAdapter
import com.d204.rumeet.ui.mypage.model.RunningActivityUiModel
import com.d204.rumeet.ui.mypage.model.toUiModel
import com.d204.rumeet.util.toDate
import com.d204.rumeet.util.toRecord
import kotlinx.coroutines.launch

class RunningRecordFragment : BaseFragment<FragmentRunningRecordBinding, MyPageViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_record
    override val viewModel: MyPageViewModel by navGraphViewModels<MyPageViewModel>(R.id.navigation_mypage){defaultViewModelProviderFactory}

    override fun initStartView() {
        binding.contentRunningRecordNoResult.tvContentNoResultMessage.text = "러닝 데이터가 없습니다."
        initDatePicker()
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.runningRecord.collect{
                    val summaryData = it.successOrNull()?.summaryData
                    binding.tvRunningRecordAverageDistance.text =  summaryData?.totalDistance.toString()
                    binding.tvRunningRecordAverageTime.text = summaryData?.totalTime.toString()
                    binding.tvRunningRecordAveragePace.text = summaryData?.averagePace?.toRecord()

                    val activityList = it.successOrNull()?.raceList?.map { model -> model.toUiModel() } ?: emptyList()
                    initActivityListAdapter(activityList)
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initActivityListAdapter(list: List<RunningActivityUiModel>){
        val activityAdapter = RunningActivityListAdapter().apply {
            submitList(list)
        }

        with(binding.rvRunningRecordActivity){
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = activityAdapter
        }
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
            setViewModel(viewModel)
        }
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }
}