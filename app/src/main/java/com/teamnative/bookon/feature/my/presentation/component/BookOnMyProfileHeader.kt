package com.teamnative.bookon.feature.my.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppElevation
import com.teamnative.bookon.core.designsystem.theme.AppIconSize
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.BookOnTheme
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography

/**
 * 내 서재 상단에 프로필 이미지와 사용자 정보를 Figma 구조로 표시한다.
 * 프로필 이미지 편집 버튼은 화면의 Photo Picker를 열도록 상위 Route에 이벤트를 전달한다.
 */
@Composable
fun BookOnMyProfileHeader(
    userNameText: String,
    studentInfoText: String,
    profileImageUrl: String?,
    isProfileImageUploading: Boolean,
    onProfileImageEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImageWithEditButton(
            profileImageUrl = profileImageUrl,
            isUploading = isProfileImageUploading,
            onEditClick = onProfileImageEditClick,
        )

        Spacer(modifier = Modifier.size(AppSpacing.Item))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userNameText,
                style = bookOnTypography.sectionTitle,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Tiny))

            Text(
                text = studentInfoText,
                style = bookOnTypography.caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** 프로필 이미지를 원형으로 표시하고 우측 하단에 편집 버튼을 겹쳐 표시한다. */
@Composable
private fun ProfileImageWithEditButton(
    profileImageUrl: String?,
    isUploading: Boolean,
    onEditClick: () -> Unit,
) {
    val placeholderPainter = painterResource(R.drawable.main_profile)

    Box(modifier = Modifier.size(AppIconSize.Avatar)) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = stringResource(R.string.profile_image_description),
            placeholder = placeholderPainter,
            error = placeholderPainter,
            fallback = placeholderPainter,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(AppIconSize.Avatar)
                .clip(CircleShape),
        )

        IconButton(
            onClick = onEditClick,
            enabled = !isUploading,
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Surface(
                modifier = Modifier.size(AppIconSize.ProfileEditBadge),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = AppElevation.Button,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit_profile),
                        contentDescription = stringResource(R.string.profile_image_edit_description),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookOnMyProfileHeaderPreview() {
    BookOnTheme {
        BookOnMyProfileHeader(
            userNameText = "홍길동 님",
            studentInfoText = "10기 · 소프트웨어 개발과",
            profileImageUrl = null,
            isProfileImageUploading = false,
            onProfileImageEditClick = {},
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal),
        )
    }
}
