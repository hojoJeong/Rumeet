package com.d204.rumeet.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.d204.rumeet.ui.login.LoginActivity
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel>(layoutId: Int) : Fragment(layoutId) {

    private var _binding: T? = null
    val binding get()= requireNotNull(_binding)

    /**
     * setContentView로 호출할 Layout의 리소스 Id.
     * ex) R.layout.activity_main
     */
    abstract val layoutResourceId: Int

    /**
     * viewModel 로 쓰일 변수.
     */
    abstract val viewModel: R

    /**
     * 두번째로 호출.
     * 데이터 바인딩 및 Coroutine 설정.
     * ex) lifecyelScope.launch{}, lifecycleScope.launchWhenStarted{] ..
     */
    abstract fun initStartView()

    /**
     * 두번째로 호출.
     * 데이터 바인딩 및 rxjava 설정.
     * ex) rxjava observe, databinding observe..
     */
    abstract fun initDataBinding()

    /**
     * 바인딩 이후에 할 일을 여기에 구현.
     * 그 외에 설정할 것이 있으면 이곳에서 설정.
     * 클릭 리스너도 이곳에서 설정.
     */
    abstract fun initAfterBinding()

    private var isSetBackButtonValid = false

    /**
     * Loading Dialog 관련해서 사용할 변수
     */
    private val mLoadingDialog: LoadingDialogFragment by lazy { LoadingDialogFragment() }

    /**
     * Exception을 처리할 SharedFlow
     */
    protected var exception: SharedFlow<Throwable>? = null
    private var toast: Toast? = null


    init {
        lifecycleScope.launchWhenStarted {
            launch {
                exception?.collectLatest { exception ->
                    showToastMessage(exception)
                }
            }

            launch {
                viewModel.errorEvent.collectLatest { e ->
                    dismissLoadingDialog()
                    showToastMessage(e)
                }
            }

            launch {
                viewModel.loadingEvent.collectLatest {
                    if(it) showLoadingDialog()
                    else dismissLoadingDialog()
                }
            }

            launch {
                viewModel.needLoginEvent.collectLatest { loginCheck() }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initStartView()
        initDataBinding()
        initAfterBinding()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        toast?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 로딩 다이얼로그, 즉 로딩창을 띄워줌.
    // 네트워크가 시작될 때 사용자가 무작정 기다리게 하지 않기 위해 작성.
    private fun showLoadingDialog() {
        mLoadingDialog.show(childFragmentManager, mLoadingDialog.tag)
    }

    // 띄워 놓은 로딩 다이얼로그를 없앰.
    private fun dismissLoadingDialog() {
        if (mLoadingDialog.isVisible) {
            mLoadingDialog.dismiss()
        }
    }

    // Toast Message 관련 함수
    protected fun showToastMessage(e: Throwable?) {
        toast?.cancel()
        toast = Toast.makeText(activity, e?.cause?.message ?: "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT)?.apply { show() }
    }

    // Toast Message 관련 함수
    protected fun toastMessage(message: String) {
        toast?.cancel()
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)?.apply { show() }
    }

    // navigation 중복체크 관리 <- checkNavigation 대신 사용할것
    protected fun Fragment.navigate(directions: NavDirections) {
        val controller = findNavController()
        val currentDestination = (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
        if (currentDestination == this.javaClass.name) {
            controller.navigate(directions)
        }
    }

    // Home 화면으로 이동
//    protected fun navigateToHomeFragment(navOptions: NavOptions? = null) {
//        val mainFragmentId = com.dida.android.R.id.homeFragment
//        if (findNavController().currentDestination?.id != mainFragmentId) {
//            val result = findNavController().popBackStack(mainFragmentId, false)
//            if (!result) {
//                findNavController().navigate(NavigationGraphDirections.actionMainFragment(), navOptions)
//            }
//        }
//    }

    // 미 로그인시 로그인 로직
    private fun loginCheck() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        registerForActivityResult.launch(intent)
    }

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == 0) navigateToHomeFragment(null)
        }
}
