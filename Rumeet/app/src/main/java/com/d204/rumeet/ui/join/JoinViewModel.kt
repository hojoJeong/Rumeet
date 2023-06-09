package com.d204.rumeet.ui.join

import android.content.ContentValues.TAG
import android.util.Log
import com.d204.rumeet.data.remote.dto.DuplicateInfoException
import com.d204.rumeet.domain.model.user.ModifyProfileAndNickNameDomainModel
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.sign.CheckDuplicateInfoUseCase
import com.d204.rumeet.domain.usecase.sign.EmailSignUpUseCase
import com.d204.rumeet.domain.usecase.sign.SocialSignUpUseCase
import com.d204.rumeet.domain.usecase.user.ModifyProfileImgAndNickNameUseCase
import com.d204.rumeet.domain.usecase.user.ModifyUserDetailInfoUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.d204.rumeet.ui.join.addtional_info.AdditionalInfoAction
import com.d204.rumeet.ui.join.id.JoinIdAction
import com.d204.rumeet.ui.join.nickname.JoinNicknameAction
import com.d204.rumeet.ui.join.password.JoinPasswordAction
import com.d204.rumeet.ui.mypage.EditUserInfoAction
import com.d204.rumeet.ui.mypage.model.UserDetailInfoUiModel
import com.d204.rumeet.ui.mypage.model.toDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val checkDuplicateInfoUseCase: CheckDuplicateInfoUseCase,
    private val emailSignUpUseCase: EmailSignUpUseCase,
    private val socialSignUpUseCase: SocialSignUpUseCase,
    private val modifyUserDetailInfoUseCase: ModifyUserDetailInfoUseCase,
    private val modifyProfileImgAndNickNameUseCase: ModifyProfileImgAndNickNameUseCase
) : BaseViewModel() {

    private val _joinIdAction: MutableSharedFlow<JoinIdAction> = MutableSharedFlow()
    val joinIdAction: SharedFlow<JoinIdAction> get() = _joinIdAction.asSharedFlow()

    private val _joinNicknameAction: MutableSharedFlow<JoinNicknameAction> = MutableSharedFlow()
    val joinNicknameAction: SharedFlow<JoinNicknameAction> get() = _joinNicknameAction.asSharedFlow()

    private val _joinPasswordAction: MutableSharedFlow<JoinPasswordAction> = MutableSharedFlow()
    val joinPasswordAction: SharedFlow<JoinPasswordAction> get() = _joinPasswordAction.asSharedFlow()

    private val _additionalInfoAction: MutableSharedFlow<AdditionalInfoAction> = MutableSharedFlow()
    val additionalInfoAction: SharedFlow<AdditionalInfoAction> get() = _additionalInfoAction.asSharedFlow()

    private val _editUserInfoEvent: MutableSharedFlow<EditUserInfoAction> = MutableSharedFlow()
    val editUserInfoEvent: SharedFlow<EditUserInfoAction> get() = _editUserInfoEvent.asSharedFlow()


    private val _resultEditUserProfile: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val resultEditUserProfile: StateFlow<Boolean> get() = _resultEditUserProfile.asStateFlow()

    val joinInfo: JoinModel = JoinModel()
    val editUserInfo: UserDetailInfoUiModel = UserDetailInfoUiModel()
    val editProfile: EditProfile = EditProfile()

    /**
     * 아이디 중복체크
     * type = 1(닉네임), 2(이메일)
     * @param id - 중복체크하려는 id
     * */
    fun idValidation(id: String) {
        baseViewModelScope.launch {
            showLoading()
            checkDuplicateInfoUseCase(2, id)
                .onSuccess {
                    _joinIdAction.emit(JoinIdAction.NavigateNicknameFragment)
                    joinInfo.id = id
                }
                .onError { e ->
                    if (e is DuplicateInfoException) _joinIdAction.emit(JoinIdAction.IdDuplicate)
                    else catchError(e)
                }
            dismissLoading()
        }
    }

    /**
     * 닉네임 중복체크
     * type = 1(닉네임), 2(이메일)
     * @param nickname - 중복체크하려는 nick
     * */
    fun nicknameValidation(nickname: String) {
        baseViewModelScope.launch {
            showLoading()
            checkDuplicateInfoUseCase(1, nickname)
                .onSuccess {
                    joinInfo.nickname = nickname
                    _joinNicknameAction.emit(JoinNicknameAction.PassNicknameValidation(nickname))
                }
                .onError { e ->
                    if (e is DuplicateInfoException) _joinNicknameAction.emit(JoinNicknameAction.DuplicateNickname)
                    else catchError(e)
                }
            dismissLoading()
        }
    }

    /**
     * 소셜로그인 회원가입
     * oauth로 id, password를 생성
     * profile img는 null로 넣어도 상관없음 -> 서버 내부 로직 처리
     * */
    fun socialSignUp() {
        baseViewModelScope.launch {
            showLoading()
            socialSignUpUseCase.invoke(
                joinInfo.socialJoinModel?.oauth!!,
                joinInfo.nickname,
                joinInfo.socialJoinModel?.profileImgUrl!!,
                joinInfo.weight,
                joinInfo.height,
                joinInfo.gender,
                joinInfo.age,
                joinInfo.profileImg
            ).onSuccess {
                _additionalInfoAction.emit(AdditionalInfoAction.SignUpSuccess)
            }.onError { e -> catchError(e) }
            dismissLoading()
        }
    }

    /**
     * 이메일로 회원가입
     * profile img는 File로 전달
     * */
    fun emailSignUp() {
        baseViewModelScope.launch {
            showLoading()
            emailSignUpUseCase.invoke(
                joinInfo.id,
                joinInfo.password,
                joinInfo.nickname,
                joinInfo.weight,
                joinInfo.height,
                joinInfo.gender,
                joinInfo.age,
                joinInfo.profileImg
            ).onSuccess {
                _additionalInfoAction.emit(AdditionalInfoAction.SignUpSuccess)
            }.onError { e -> catchError(e) }
            dismissLoading()
        }
    }

    fun editUserInfo(){
        baseViewModelScope.launch {
                    showLoading()
                    if(modifyUserDetailInfoUseCase.invoke(editUserInfo.toDomainModel())){
                        _additionalInfoAction.emit(AdditionalInfoAction.SignUpSuccess)
                dismissLoading()
            } else {
                Log.d("TAG", "editUserInfo: 회원 정보 수정 오류")
            }
        }
    }

    fun editProfile(){
        baseViewModelScope.launch {
            showLoading()
            if(modifyProfileImgAndNickNameUseCase.invoke(editProfile.toDoMainModel())){
                Log.d(TAG, "성공: $editProfile")
                _resultEditUserProfile.value = true
                dismissLoading()
            } else {
                Log.d("TAG", "editProfile: 프로필 수정 오류")
            }
        }
    }

    // 닉네임 중복검사
    fun checkNicknameValidation() {
        baseViewModelScope.launch {
            _joinNicknameAction.emit(JoinNicknameAction.CheckNicknameValidation)
        }
    }

    // 아이디 중복검사 하기
    fun checkIdValidation() {
        baseViewModelScope.launch {
            _joinIdAction.emit(JoinIdAction.CheckIdDuplicate)
        }
    }

    // 갤러리 불러오기
    fun navigationToGallery() {
        baseViewModelScope.launch {
            _joinNicknameAction.emit(JoinNicknameAction.NavigateGallery)
        }
    }

    // 비밀번호 유효성 검증
    fun checkPasswordValidation() {
        baseViewModelScope.launch {
            _joinPasswordAction.emit(JoinPasswordAction.CheckPasswordValidation)
        }
    }

    /**
     * 회원가입 실행
     * @param socialType - 이메일(false), 소셜(true)
     * */
    fun signUp(socialType: Boolean) {
        baseViewModelScope.launch {
            if (socialType) _additionalInfoAction.emit(AdditionalInfoAction.SocialSignUp)
            else _additionalInfoAction.emit(AdditionalInfoAction.EmailSignUp)
        }
    }
}