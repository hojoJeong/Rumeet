package com.d204.rumeet.ui.join.nickname

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentJoinNicknameBinding
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.components.SingleLineEditText
import com.d204.rumeet.ui.join.JoinViewModel
import com.d204.rumeet.ui.join.SocialJoinModel
import com.d204.rumeet.util.getAbsolutePath
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

class JoinNicknameFragment : BaseFragment<FragmentJoinNicknameBinding, JoinViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_join_nickname

    override val viewModel: JoinViewModel by activityViewModels()
    private val args: JoinNicknameFragmentArgs by navArgs()
    private var imageFile : File? = null
    private var socialLogin = false

    private val galleryLauncher =
        registerForActivityResult((ActivityResultContracts.StartActivityForResult())) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data!!
                imageFile = File(getAbsolutePath(uri, requireContext()))
                binding.ivProfileImg.setImageURI(uri)
            }
        }

    override fun initStartView() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent

        if(args.reset){
            Glide.with(requireContext()).load(args.profileImg).into(binding.ivProfileImg)
            binding.tvAgreeTermsOfUsers.visibility = View.GONE
        } else {
            // args의 profile img가 null이면 아이디, 아니면 소셜로그인의 회원가입
            if (args.oauth != 1L) {
                socialLogin = true
                viewModel.joinInfo.socialJoinModel = SocialJoinModel(args.oauth, args.profileImg)

                Glide.with(requireContext())
                    .load(args.profileImg)
                    .into(binding.ivProfileImg)
            }
        }
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            launch {
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
                        is JoinNicknameAction.PassNicknameValidation -> {
                            if (args.reset) {
                                viewModel.editProfile.curProfile = args.profileImg
                                viewModel.editProfile.name = binding.editNickname.keyword
                                viewModel.editProfile.editProfile = imageFile
                                viewModel.editProfile.id = args.userId
                                Log.d(TAG, "initDataBinding: ${viewModel.editProfile}")
                                viewModel.editProfile()
                            } else {
                                viewModel.joinInfo.nickname = it.nickname
                                viewModel.joinInfo.profileImg = imageFile
                                if (!socialLogin) navigate(JoinNicknameFragmentDirections.actionJoinNickNameFragmentToJoinPasswordFragment())
                                else navigate(JoinNicknameFragmentDirections.actionJoinNickNameFragmentToAdditionalInfoFragment())
                            }
                        }
                        is JoinNicknameAction.NavigateGallery -> {
                            navigateGallery()
                        }
                    }
                }
            }
            launch {
                viewModel.resultEditUserProfile.collect{
                    if(it){
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
        binding.btnContinue.setContent("계속하기")
        binding.editNickname.setEditTextType(
            SingleLineEditText.SingUpEditTextType.NORMAL,
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