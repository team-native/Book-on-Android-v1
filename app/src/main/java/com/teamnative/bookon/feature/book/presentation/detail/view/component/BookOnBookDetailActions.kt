package com.teamnative.bookon.feature.book.presentation.detail.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.teamnative.bookon.R
import com.teamnative.bookon.core.designsystem.theme.AppComponentSize
import com.teamnative.bookon.core.designsystem.theme.AppRadius
import com.teamnative.bookon.core.designsystem.theme.AppSpacing
import com.teamnative.bookon.core.designsystem.theme.bookOnTypography
import com.teamnative.bookon.core.ui.component.button.BookOnPrimaryButton
import com.teamnative.bookon.feature.book.presentation.detail.viewmodel.BookOnBookDetailScreenUiState

@Composable
fun BookOnBookDetailActions(
    uiState: BookOnBookDetailScreenUiState,
    isRefreshing: Boolean,
    onFavoriteClick: () -> Unit,
) {
    val isPending = uiState.isFavoriteSubmitting || isRefreshing
    val loadingDescription = stringResource(R.string.state_loading)
    val favoriteDescription =
        stringResource(
            if (uiState.isFavorite) {
                R.string.favorite_remove_description
            } else {
                R.string.favorite_add_description
            },
        )
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = AppSpacing.BookDetailFavoriteHorizontal,
                        vertical = AppSpacing.Item,
                    ),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconToggleButton(
                checked = uiState.isFavorite,
                onCheckedChange = { onFavoriteClick() },
                enabled = !isPending,
                modifier =
                    Modifier
                        .size(AppComponentSize.BookDetailFavoriteTouchSize)
                        .testTag("book_detail_favorite")
                        .semantics {
                            contentDescription = favoriteDescription
                            if (isPending) {
                                stateDescription = loadingDescription
                            }
                        },
            ) {
                if (uiState.isFavoriteSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(AppComponentSize.BookDetailFavoriteWidth),
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = AppRadius.Progress,
                    )
                } else {
                    Image(
                        painter =
                            painterResource(
                                if (uiState.isFavorite) {
                                    R.drawable.book_detail_favorite_filled
                                } else {
                                    R.drawable.book_detail_favorite_outline
                                },
                            ),
                        contentDescription = null,
                        modifier =
                            Modifier.size(
                                width = AppComponentSize.BookDetailFavoriteWidth,
                                height = AppComponentSize.BookDetailFavoriteHeight,
                            ),
                    )
                }
            }
            BookOnPrimaryButton(
                text =
                    stringResource(
                        if (uiState.isFavorite) {
                            R.string.book_detail_favorite_remove
                        } else {
                            R.string.book_detail_favorite_add
                        },
                    ),
                onClick = onFavoriteClick,
                modifier =
                    Modifier
                        .weight(1f)
                        .heightIn(min = AppComponentSize.BookDetailActionMinHeight)
                        .testTag("book_detail_favorite_button"),
                enabled = !isPending,
                loading = uiState.isFavoriteSubmitting,
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(AppRadius.LargeCard),
                textStyle = bookOnTypography.bookDetailAction,
                fixedHeight = null,
            )
        }
    }
}
