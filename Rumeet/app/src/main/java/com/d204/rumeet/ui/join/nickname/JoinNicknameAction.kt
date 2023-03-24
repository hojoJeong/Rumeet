package com.d204.rumeet.ui.join.nickname

import android.graphics.Paint.Join

sealed class JoinNicknameAction {
    object CheckNicknameValidation : JoinNicknameAction()
    object NavigateJoinPassword : JoinNicknameAction()
    object DuplicateNickname : JoinNicknameAction()
    object NavigateGallery : JoinNicknameAction()
}