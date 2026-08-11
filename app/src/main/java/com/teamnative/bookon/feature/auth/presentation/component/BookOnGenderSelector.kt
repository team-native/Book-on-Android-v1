package com.teamnative.bookon.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.feature.auth.presentation.signup.BookOnGender

/**
 * 성별 선택 입력 섹션을 라벨과 옵션 Row로 표시한다.
 * 선택 이벤트는 성별 값으로 호출 화면에 전달한다.
 */
@Composable
fun BookOnGenderSelector(
    selectedGender: BookOnGender?,
    onGenderSelected: (BookOnGender) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
    ) {
        Text(
            text = stringResource(R.string.gender),
            style = bookOnTypography.fieldLabel,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Content)) {
            BookOnGender.entries.forEach { gender ->
                BookOnOptionButton(
                    text = when (gender) {
                        BookOnGender.MALE -> stringResource(R.string.male)
                        BookOnGender.FEMALE -> stringResource(R.string.female)
                    },
                    selected = selectedGender == gender,
                    onSelected = { onGenderSelected(gender) },
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
