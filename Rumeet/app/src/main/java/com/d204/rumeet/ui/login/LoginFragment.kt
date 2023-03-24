package com.d204.rumeet.ui.login

import android.util.Log
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentLoginBinding
import com.d204.rumeet.ui.activities.MainActivity
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.components.FilledEditText
import com.d204.rumeet.util.startActivityAfterClearBackStack
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels()

    override fun initStartView() {
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.navigationEvent.collectLatest {
                when (it) {
                    is LoginNavigationAction.EmailLogin -> if(emailLoginValidation()) viewModel.doEmailLogin(
                        binding.editLoginId.inputText,
                        binding.editLoginPassword.inputText,
                        binding.btnLoginAuto.isChecked
                    )
                    is LoginNavigationAction.LoginFailed -> showLoginFailedDialog()
                    is LoginNavigationAction.LoginSuccess -> requireActivity().startActivityAfterClearBackStack(MainActivity::class.java)
                    is LoginNavigationAction.KakaoLogin -> kakaoLogin()
                    is LoginNavigationAction.NavigateJoin ->
                        navigate(LoginFragmentDirections.actionLoginFragmentToJoinIdFragment())
                    is LoginNavigationAction.NeedJoinFirst -> { navigate(LoginFragmentDirections.actionLoginFragmentToJoinNickNameFragment(oauth = it.oauth, profileImg = it.profileImg)) }
                    is LoginNavigationAction.NavigateFindAccount -> { navigate(LoginFragmentDirections.actionLoginFragmentToFindAccountFragment()) }

                    // Todo Naver api 승인 후 작업
                    is LoginNavigationAction.NaverLogin -> {  }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.editLoginId.setEditTextType(FilledEditText.FilledEditTextType.ID, "ID")
        binding.editLoginPassword.setEditTextType(FilledEditText.FilledEditTextType.PASSWORD, "비밀번호")
    }

    private fun emailLoginValidation() : Boolean{
        return !(binding.editLoginId.inputText == "" || binding.editLoginPassword.inputText == "")
    }

    private fun showLoginFailedDialog(){
        val dialog = DefaultAlertDialog(
            alertModel = AlertModel(title = "알림 메시지", content = "아이디/비밀번호를 확인해주세요", buttonText = "확인")
        )
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    private fun kakaoLogin(){
        val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            // 로그인 실패
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> toastMessage("접근이 거부 됨(동의 취소)")
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> toastMessage("유효하지 않은 앱")
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> toastMessage("인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> toastMessage("요청 파라미터 오류")
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> toastMessage("유효하지 않은 scope ID")
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> toastMessage("설정이 올바르지 않음(android key hash)")
                    error.toString() == AuthErrorCause.ServerError.toString() -> toastMessage("서버 내부 에러")
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> toastMessage("앱이 요청 권한이 없음")
                    else -> toastMessage("카카오톡의 미로그인")
                }
            }
            //로그인 성공
            else if (token != null) {
                viewModel.doKakaoLogin(token.accessToken)
            } else {
                Log.d("TAG", "kakaoLogin: error")
            }
        }

        // 카카오톡 설치여부 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = kakaoCallback)
        }
    }
}