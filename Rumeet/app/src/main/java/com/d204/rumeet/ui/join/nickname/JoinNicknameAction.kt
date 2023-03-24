package com.d204.rumeet.ui.join.nickname

sealed class JoinNicknameAction {
    object CheckNicknameValidation : JoinNicknameAction()
    object PassNicknameValidation : JoinNicknameAction()
    object DuplicateNickname : JoinNicknameAction()
    object NavigateGallery : JoinNicknameAction()
}