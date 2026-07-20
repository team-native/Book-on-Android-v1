package com.teamnative.bookon.core.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class BookOnNavigationItemUiModel(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
)
