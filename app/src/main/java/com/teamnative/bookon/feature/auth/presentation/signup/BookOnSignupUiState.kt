package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.runtime.Immutable
import androidx.annotation.StringRes
import com.teamnative.bookon.R
import com.teamnative.bookon.core.ui.model.BookOnTextFieldUiModel
import com.teamnative.bookon.feature.auth.presentation.model.BookOnDropdownFieldUiModel

/** 회원가입 단계에서 선택 가능한 성별 값이다. */
enum class BookOnGender {
    MALE,
    FEMALE,
}

/** 회원가입에서 한 가지만 선택할 수 있는 학과 목록이다. */
enum class BookOnDepartment(@param:StringRes val textResId: Int) {
    SOFTWARE_DEVELOPMENT(R.string.department_software),
    IOT(R.string.department_iot),
    AI(R.string.department_ai),
}

@Immutable
data class BookOnSignupUiState(
    val stepText: String,
    val title: String,
    val description: String,
    val email: BookOnTextFieldUiModel,
    val name: BookOnTextFieldUiModel,
    val selectedGender: BookOnGender? = null,
    val department: BookOnDropdownFieldUiModel,
    val nextEnabled: Boolean = true,
)
