package com.d204.rumeet.ui.find_account

import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentFindAccountBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.components.FilledEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import org.apache.commons.lang3.Conversion.byteToHex
import java.security.MessageDigest
import java.util.regex.Pattern

@AndroidEntryPoint
class FindAccountFragment : BaseFragment<FragmentFindAccountBinding, FindAccountViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_find_account

    override val viewModel: FindAccountViewModel by viewModels()

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.findAccountAction.collectLatest {
                when (it) {
                    is FindAccountAction.RequestAuthenticationCode -> {
                        val pattern = android.util.Patterns.EMAIL_ADDRESS
                        if(pattern.matcher(binding.editId.inputText).matches()) viewModel.requestCode(binding.editId.inputText)
                        else showCheckIdDialog()
                    }
                    is FindAccountAction.FailRequestAuthenticationCode -> {
                        showCheckIdDialog()
                    }
                    is FindAccountAction.StartAuthentication -> {
                        binding.btnCodeCheck.text = getString(R.string.content_authentication_code)
                    }
                    is FindAccountAction.CheckAuthentication -> {
                        viewModel.checkAuthenticationCode(
                            hashingSHA256(binding.editAuthenticationCode.inputText)
                        )
                    }
                    is FindAccountAction.FailAuthentication -> {
                        showFailAuthenticationDialog()
                    }
                    is FindAccountAction.SuccessAuthentication -> {
                        // TODO navigate
                    }
                    is FindAccountAction.TimeOutAuthentication -> {
                        showTimeOutDialog()
                        binding.btnCodeCheck.text = getString(R.string.content_authentication_code)
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.editId.setEditTextType(
            FilledEditText.FilledEditTextType.ID,
            getString(R.string.title_find_account_id_input)
        )
        binding.editAuthenticationCode.setEditTextType(
            FilledEditText.FilledEditTextType.NORMAL,
            getString(R.string.content_authentication_code_hint)
        )
    }

    private fun hashingSHA256(code : String) : String{
        val md = MessageDigest.getInstance("SHA-256").digest(code.toByteArray())
        val test = md.joinToString("") { "%02x".format(it) }
        return md.joinToString("") { "%02x".format(it) }
    }

    private fun showFailAuthenticationDialog() {
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(
                title = "알림 메시지",
                content = getString(R.string.content_check_authentication_code),
                buttonText = "확인"
            )
        )
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    private fun showTimeOutDialog() {
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(
                title = "알림 메시지",
                content = getString(R.string.content_time_out),
                buttonText = "확인"
            )
        )
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    private fun showCheckIdDialog() {
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(
                title = "알림 메시지",
                content = getString(R.string.content_check_id),
                buttonText = "확인"
            )
        )
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(AUTHENTICATION_TIME, AUTHENTICATION_TIME) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

            }
        }
    }

    companion object {
        private const val AUTHENTICATION_TIME = 180000L
    }
}