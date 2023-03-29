package com.d204.rumeet.ui.join.nickname

sealed class JoinNicknameAction {
    object CheckNicknameValidation : JoinNicknameAction()
    class PassNicknameValidation(val nickname : String) : JoinNicknameAction()
    object DuplicateNickname : JoinNicknameAction()
    object NavigateGallery : JoinNicknameAction()
}