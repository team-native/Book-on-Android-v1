package com.teamnative.bookon.feature.auth.presentation.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.teamnative.bookon.R

private const val DepartmentRadioButtonScale = 0.7f
private val DepartmentMenuMinWidth = 240.dp

/** 학교 정보 입력 단계의 샘플 상태와 화면 이벤트를 연결한다. */
@Composable
fun BookOnSignupRoute(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: BookOnRegistrationViewModel = hiltViewModel(),
) {
    val registrationState by viewModel.state.collectAsStateWithLifecycle()
    var isDepartmentMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val selectedDepartmentText = registrationState.department?.let { department ->
        stringResource(department.textResId)
    }.orEmpty()
    val uiState = defaultSignupUiState(
        email = registrationState.email,
        name = registrationState.name,
        selectedGender = registrationState.gender,
        selectedDepartmentText = selectedDepartmentText,
        isDepartmentMenuExpanded = isDepartmentMenuExpanded,
    )

    BookOnSignupScreen(
        modifier = if (isDepartmentMenuExpanded) Modifier.blur(radius = 8.dp) else Modifier,
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                BookOnSignupScreenEvent.BackClicked -> onBackClick()
                is BookOnSignupScreenEvent.EmailChanged -> viewModel.update { it.copy(email = event.email) }
                is BookOnSignupScreenEvent.NameChanged -> viewModel.update { it.copy(name = event.name) }
                is BookOnSignupScreenEvent.GenderSelected -> viewModel.update { it.copy(gender = event.gender) }
                BookOnSignupScreenEvent.DepartmentClicked -> isDepartmentMenuExpanded = true
                BookOnSignupScreenEvent.NextClicked -> onNextClick()
            }
        },
    )

    if (isDepartmentMenuExpanded) {
        BookOnDepartmentCenterMenu(
            selectedDepartment = registrationState.department,
            onDepartmentSelected = { department ->
                viewModel.update { it.copy(department = department) }
                isDepartmentMenuExpanded = false
            },
            onDismissRequest = { isDepartmentMenuExpanded = false },
        )
    }
}

/** 학과 옵션을 중앙에 표시하고 하나를 선택하면 즉시 닫는 모달 메뉴이다. */
@Composable
private fun BookOnDepartmentCenterMenu(
    selectedDepartment: BookOnDepartment?,
    onDepartmentSelected: (BookOnDepartment) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.widthIn(min = DepartmentMenuMinWidth),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column {
                BookOnDepartment.entries.forEach { department ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    modifier = Modifier.scale(DepartmentRadioButtonScale),
                                    selected = selectedDepartment == department,
                                    onClick = null,
                                )
                                Text(text = stringResource(department.textResId))
                            }
                        },
                        onClick = { onDepartmentSelected(department) },
                    )
                }
            }
        }
    }
}
