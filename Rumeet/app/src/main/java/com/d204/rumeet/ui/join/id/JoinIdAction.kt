package com.d204.rumeet.ui.join.id

sealed class JoinIdAction {
    object NavigateNicknameFragment : JoinIdAction()
    object CheckIdDuplicate : JoinIdAction()
    object IdDuplicate : JoinIdAction()
}