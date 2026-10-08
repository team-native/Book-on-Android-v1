package com.teamnative.bookon.feature.book.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppIconSize

@Composable
fun BookOnBookFavoriteButton(
    isFavorite: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(AppComponentSize.ButtonHeight),
    ) {
        Image(
            painter = painterResource(R.drawable.common_love),
            contentDescription =
                stringResource(
                    if (isFavorite) {
                        R.string.favorite_remove_description
                    } else {
                        R.string.favorite_add_description
                    },
                ),
            modifier =
                Modifier
                    .size(AppIconSize.Small)
                    .alpha(if (isFavorite) 1f else 0.35f),
        )
    }
}
