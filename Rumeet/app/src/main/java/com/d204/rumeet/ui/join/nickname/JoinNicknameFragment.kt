package com.d204.rumeet.ui.join.nickname

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinNicknameBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.components.SingUpEditText
import com.d204.rumeet.ui.join.JoinViewModel
import com.d204.rumeet.ui.join.SocialJoinModel
import com.d204.rumeet.util.getAbsolutePath
import com.d204.rumeet.util.getMultipartData
import kotlinx.coroutines.flow.collectLatest

class JoinNicknameFragment : BaseFragment<FragmentJoinNicknameBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_nickname

    override val viewModel: JoinViewModel by activityViewModels()
    private val args: JoinNicknameFragmentArgs by navArgs()
    private var imageUri : Uri? = null


    private val galleryLauncher =
        registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                binding.ivProfileImg.setImageURI(imageUri)
            }
        }

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        // args의 profile img가 null이면 아이디, 아니면 소셜로그인의 회원가입
        if (args.oauth != 1L) {
            viewModel.joinInfo.socialJoinModel = SocialJoinModel(args.oauth, args.profileImg)
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.joinNicknameAction.collectLatest {
                when (it) {
                    is JoinNicknameAction.CheckNicknameValidation -> {
                        if (binding.editNickname.nicknameValidate) viewModel.nicknameValidation(
                            binding.editNickname.keyword
                        )
                    }
                    is JoinNicknameAction.DuplicateNickname -> {
                        binding.editNickname.setStateMessage(
                            getString(R.string.content_duplicated_nickname),
                            false
                        )
                    }
                    is JoinNicknameAction.NavigateJoinPassword -> {
                        viewModel.joinInfo.profileImg = requireContext().getMultipartData(imageUri)
                        navigate(JoinNicknameFragmentDirections.actionJoinNickNameFragmentToJoinPasswordFragment())
                    }
                    is JoinNicknameAction.NavigateGallery -> { navigateGallery() }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.btnContinue.setContent("계속하기")
        binding.editNickname.setEditTextType(
            SingUpEditText.SingUpEditTextType.NORMAL,
            getString(R.string.content_nickname_hint)
        )
    }

    private fun navigateGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }
        val chooserIntent = Intent(Intent.ACTION_CHOOSER).apply {
            putExtra(Intent.EXTRA_INTENT, intent)
            putExtra(Intent.EXTRA_TITLE, "사용할 앱을 선택해주세요")
        }
        galleryLauncher.launch(chooserIntent)
    }
}