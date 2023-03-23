package com.d204.rumeet.ui.join.id

sealed class JoinIdAction {
    class NavigateNicknameFragment(val id : String) : JoinIdAction()
    object CheckIdDuplicate : JoinIdAction()
    object IdDuplicate : JoinIdAction()
}